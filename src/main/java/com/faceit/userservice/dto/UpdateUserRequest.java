package com.faceit.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String nickname;
    private String country;
}