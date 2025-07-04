package com.faceit.userservice;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and retrieve user by id")
    void saveAndFindById() {
        var id = UUID.randomUUID();
        var now = Instant.now();

        var user = new User(
                id,
                "Jane",
                "Doe",
                "janedoe",
                "pass123",
                "jane@doe.com",
                "NL",
                now,
                now
        );

        userRepository.save(user);

        var retrieved = userRepository.findById(id);

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getEmail()).isEqualTo("jane@doe.com");
        assertThat(retrieved.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    @DisplayName("Should return empty if user not found")
    void findByIdNotFound() {
        var result = userRepository.findById(UUID.randomUUID());
        assertThat(result).isEmpty();
    }
}
