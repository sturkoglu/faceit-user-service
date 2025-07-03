package com.faceit.userservice;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.event.UserChangedEvent;
import com.faceit.userservice.event.UserEventKafkaPublisher;
import com.faceit.userservice.service.UserService;
import com.faceit.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserEventKafkaPublisher eventPublisher;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        eventPublisher = mock(UserEventKafkaPublisher.class);
        userService = new UserService(userRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should save user and publish event")
    void saveUserPublishesEvent() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Test", "User", "testuser", "password", "test@user.com", "NL", Instant.now(), Instant.now());

        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.addUser(user);

        assertThat(saved).isNotNull();
        verify(userRepository, times(1)).save(user);
        verify(eventPublisher, times(1)).publish(any(UserChangedEvent.class));
    }
    @Test
    @DisplayName("Should update user and publish event")
    void updateUserPublishesEvent() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Test", "User", "testuser", "password", "test@user.com", "NL", Instant.now(), Instant.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updated = userService.updateUser(id, user);

        assertThat(updated).isNotNull();
        assertThat(updated.getEmail()).isEqualTo("test@user.com");
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(user);
        verify(eventPublisher, times(1)).publish(any(UserChangedEvent.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void updateUserNotFoundThrows() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Test", "User", "testuser", "password", "test@user.com", "NL", Instant.now(), Instant.now());

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(id, user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("Should delete user and publish event")
    void deleteUserPublishesEvent() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Test", "User", "testuser", "secret", "test@user.com", "NL", Instant.now(), Instant.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        doNothing().when(userRepository).deleteById(id);

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
        verify(eventPublisher, times(1)).publish(any(UserChangedEvent.class));
    }
}
