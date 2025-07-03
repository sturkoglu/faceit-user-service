package com.faceit.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String nickname;
    private String password;
    private String email;
    private String country;
}