package com.faceit.userservice.repository;

import com.faceit.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
        Page<User> findByCountryIgnoreCase(String country, Pageable pageable);
        boolean existsByEmail(String email);
        boolean existsByNickname(String nickname);
}
