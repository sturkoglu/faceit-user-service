package com.faceit.userservice.controller;

import com.faceit.userservice.dto.CreateUserRequest;
import com.faceit.userservice.dto.UpdateUserRequest;
import com.faceit.userservice.dto.UserResponse;
import com.faceit.userservice.mapper.UserMapper;
import com.faceit.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        var user = UserMapper.toEntity(request);
        user.setId(UUID.randomUUID());

        var created = userService.createUser(user);

        return ResponseEntity.ok(UserMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        var user = UserMapper.toEntity(request);
        var updated = userService.updateUser(id, user);

        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public Page<UserResponse> getUsers(@RequestParam(required = false) String country,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);

        var userPage = userService.getUsersFilteredByCountry(country, pageable);

        List<UserResponse> responses = UserMapper.toResponseList(userPage.getContent());

        return new PageImpl<>(responses, userPage.getPageable(), userPage.getTotalElements());
    }
}
