package com.faceit.userservice;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.event.UserChangedEvent;
import com.faceit.userservice.repository.UserRepository;
import com.faceit.userservice.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@RecordApplicationEvents
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    private static final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save user and publish event")
    void saveUserPublishesEvent() {
        User user = new User(
                userId, "John", "Doe", "johnny", "secret", "john@doe.com", "NL", Instant.now(), Instant.now()
        );
        userService.addUser(user);

        Optional<User> found = userRepository.findById(userId);
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(applicationEvents.stream(UserChangedEvent.class)).hasSize(1);
    }

    @Test
    @DisplayName("Should update user and publish event")
    void updateUserCheckUpdatedAndPublishedEvent() {
        User user = new User(
                userId, "Alice", "Smith", "alices", "pass", "alice@smith.com", "DE", Instant.now(), Instant.now()
        );
        User savedUser = userRepository.save(user);

        savedUser.setFirstName("Alicia");
        userService.updateUser(userId, savedUser);

        Optional<User> found = userRepository.findById(userId);
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alicia");
        assertThat(applicationEvents.stream(UserChangedEvent.class)).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void updateUserNotFoundThrows() {
        UUID nonExistingUserId = UUID.randomUUID();
        User user = new User(userId, "Test", "User", "testuser", "password", "test@user.com", "NL", Instant.now(), Instant.now());

        User savedUser = userRepository.save(user);

        savedUser.setFirstName("Alicia");

        assertThatThrownBy(() -> userService.updateUser(nonExistingUserId, savedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");

        assertThat(applicationEvents.stream(UserChangedEvent.class)).hasSize(0);
    }

    @Test
    @DisplayName("Should delete user and publish event")
    void deleteUser_thenNotFound() {
        User user = new User(
                userId, "Bob", "Jones", "bobby", "pw", "bob@jones.com", "UK", Instant.now(), Instant.now()
        );
        userRepository.save(user);
        userService.deleteUser(userId);

        assertThat(userRepository.findById(userId)).isNotPresent();
        assertThat(applicationEvents.stream(UserChangedEvent.class)).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void deleteUserUserNotFoundThrows() {
        UUID nonExistingUserId = UUID.randomUUID();
        User user = new User(
                userId, "Bob", "Jones", "bobby", "pw", "bob@jones.com", "UK", Instant.now(), Instant.now()
        );
        userRepository.save(user);
        userService.deleteUser(nonExistingUserId);

        assertThat(userRepository.findById(nonExistingUserId)).isNotPresent();
        assertThat(applicationEvents.stream(UserChangedEvent.class)).hasSize(0);
    }

    @Test
    void deleteUser_thenCheckDeleted() {
        User user = new User(
                userId, "Bob", "Jones", "bobby", "pw", "bob@jones.com", "UK", Instant.now(), Instant.now()
        );
        userRepository.save(user);
        userService.deleteUser(userId);

        assertThat(userRepository.findById(userId)).isNotPresent();
    }

    @Test
    void createUser_shouldPublishEvent() {
        User user = new User(
                userId, "Eve", "Evans", "evee", "pw", "eve@evans.com", "BE", Instant.now(), Instant.now()
        );
        userService.addUser(user);

        assertThat(applicationEvents.stream(UserChangedEvent.class)).hasSize(1);
    }
}
