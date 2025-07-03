package com.faceit.userservice.service;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.event.UserChangedEvent;
import com.faceit.userservice.event.UserChangedEventAction;
import com.faceit.userservice.event.UserEventKafkaPublisher;
import com.faceit.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserEventKafkaPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByNickname(user.getNickname())) {
            throw new IllegalArgumentException("Nickname already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var saved = userRepository.save(user);
        eventPublisher.publish(new UserChangedEvent(this, UserChangedEventAction.CREATED, saved));
        return saved;
    }

    public User updateUser(UUID id, User user) {
        var existing = userRepository.findById(id);
        if (existing.isEmpty()) throw new IllegalArgumentException("User not found");

        var toUpdate = existing.get();

        toUpdate.setFirstName(user.getFirstName());
        toUpdate.setLastName(user.getLastName());
        toUpdate.setCountry(user.getCountry());

        var updated = userRepository.save(toUpdate);
        eventPublisher.publish(new UserChangedEvent(this, UserChangedEventAction.UPDATED, updated));
        return updated;
    }

    public void deleteUser(UUID id) {
        var user = userRepository.findById(id);
        user.ifPresent(u -> {
            userRepository.deleteById(id);
            eventPublisher.publish(new UserChangedEvent(this, UserChangedEventAction.DELETED, u));
        });
    }

    public Page<User> getUsersFilteredByCountry(String country, Pageable pageable) {
        if (country != null && !country.isEmpty()) {
            return userRepository.findByCountryIgnoreCase(country, pageable);
        }

        return userRepository.findAll(pageable);
    }
}
