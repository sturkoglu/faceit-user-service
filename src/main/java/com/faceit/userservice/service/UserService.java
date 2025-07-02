package com.faceit.userservice.service;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.event.UserChangedEvent;
import com.faceit.userservice.event.UserChangedEventAction;
import com.faceit.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public User addUser(User user) {
        User saved = userRepository.save(user);
        eventPublisher.publishEvent(new UserChangedEvent(this, UserChangedEventAction.CREATED, saved));
        return saved;
    }

    public User updateUser(UUID id, User user) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isEmpty()) throw new IllegalArgumentException("User not found");

        User toUpdate = existing.get();

        toUpdate.setFirstName(user.getFirstName());
        toUpdate.setLastName(user.getLastName());
        toUpdate.setNickname(user.getNickname());
        toUpdate.setEmail(user.getEmail());
        toUpdate.setPassword(user.getPassword());
        toUpdate.setCountry(user.getCountry());
        toUpdate.setUpdatedAt(Instant.now());

        User updated = userRepository.save(toUpdate);
        eventPublisher.publishEvent(new UserChangedEvent(this, UserChangedEventAction.UPDATED, updated));
        return updated;
    }

    public void deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> {
            userRepository.deleteById(id);
            eventPublisher.publishEvent(new UserChangedEvent(this, UserChangedEventAction.DELETED, u));
        });
    }

    public Page<User> getUsersFilteredByCountry(String country, Pageable pageable) {
        if (country != null && !country.isEmpty()) {
            return userRepository.findByCountryIgnoreCase(country, pageable);
        }
        return userRepository.findAll(pageable);
    }
}
