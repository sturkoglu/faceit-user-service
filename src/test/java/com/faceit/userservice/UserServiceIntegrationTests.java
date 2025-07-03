package com.faceit.userservice;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.event.UserChangedEvent;
import com.faceit.userservice.repository.UserRepository;
import com.faceit.userservice.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"user-events"}, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaConsumer<String, UserChangedEvent> consumer;

    private static final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Set up Kafka consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
                "testGroup", "false", embeddedKafkaBroker
        );
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserChangedEvent.class.getName());

        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singleton("user-events"));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    @DisplayName("Should save user and publish event")
    void save_userPublishesEvent() {
        var user = new User(
                userId, "John", "Doe", "johnny", "secret", "john@doe.com", "NL", Instant.now(), Instant.now()
        );
        userService.createUser(user);

        var foundUser = userRepository.findById(userId);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(hasUserChangedEvent(userId)).isTrue();
    }


    @Test
    @DisplayName("Should update user and publish event")
    void update_userCheckUpdatedAndPublishedEvent() {
        var user = new User(
                userId, "Alice", "Smith", "alices", "pass", "alice@smith.com", "DE", Instant.now(), Instant.now()
        );
        var savedUser = userRepository.save(user);

        savedUser.setFirstName("Alicia");
        userService.updateUser(userId, savedUser);

        var found = userRepository.findById(userId);
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alicia");
        assertThat(hasUserChangedEvent(userId)).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void update_userNotFoundThrows() {
        var nonExistingUserId = UUID.randomUUID();
        var user = new User(userId, "Test", "User", "testuser", "password", "test@user.com", "NL", Instant.now(), Instant.now());

        var savedUser = userRepository.save(user);

        savedUser.setFirstName("Alicia");

        assertThatThrownBy(() -> userService.updateUser(nonExistingUserId, savedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");

        assertThat(hasUserChangedEvent(nonExistingUserId)).isFalse();
    }

    @Test
    @DisplayName("Should delete user and publish event")
    void delete_user() {
        var user = new User(
                userId, "Bob", "Jones", "bobby", "pw", "bob@jones.com", "UK", Instant.now(), Instant.now()
        );
        var savedUser = userRepository.save(user);
        userService.deleteUser(userId);

        assertThat(userRepository.findById(userId)).isNotPresent();
        assertThat(hasUserChangedEvent(userId)).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void delete_userUserNotFoundThrows() {
        var nonExistingUserId = UUID.randomUUID();
        var user = new User(
                userId, "Bob", "Jones", "bobby", "pw", "bob@jones.com", "UK", Instant.now(), Instant.now()
        );
        userRepository.save(user);
        userService.deleteUser(nonExistingUserId);

        assertThat(userRepository.findById(nonExistingUserId)).isNotPresent();
        assertThat(hasUserChangedEvent(nonExistingUserId)).isFalse();
    }

    private boolean hasUserChangedEvent(UUID userId) {
        ConsumerRecords<String, UserChangedEvent> records = consumer.poll(Duration.ofSeconds(1));

        for (ConsumerRecord<String, UserChangedEvent> record : records) {
            UserChangedEvent event = record.value();
            if (event.getUser().getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}
