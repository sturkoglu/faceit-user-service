package com.faceit.userservice.mapper;

import com.faceit.userservice.dto.CreateUserRequest;
import com.faceit.userservice.dto.UpdateUserRequest;
import com.faceit.userservice.dto.UserResponse;
import com.faceit.userservice.entity.User;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCountry(request.getCountry());
        return user;
    }

    public User toEntity(UpdateUserRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setNickname(request.getNickname());
        user.setCountry(request.getCountry());
        return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setCountry(user.getCountry());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public void updateUserFromRequest(CreateUserRequest request, User user) {
        if (request == null || user == null) return;
        // Only update non-null properties (imitating IGNORE nulls strategy)
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getNickname() != null) user.setNickname(request.getNickname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        if (request.getCountry() != null) user.setCountry(request.getCountry());
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) return null;
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(toResponse(user));
        }
        return responses;
    }
}
