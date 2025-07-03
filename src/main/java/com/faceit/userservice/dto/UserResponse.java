package com.faceit.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String country;
    private Instant createdAt;
    private Instant updatedAt;
}