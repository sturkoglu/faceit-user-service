package com.faceit.userservice;

import com.faceit.userservice.entity.User;
import com.faceit.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        var users = List.of(
                new User(UUID.randomUUID(), "Alice", "Smith", "alices", "pass", "alice@smith.com", "NL", Instant.now(), Instant.now()),
                new User(UUID.randomUUID(), "Bob", "Jones", "bobby", "pass", "bob@jones.com", "BE", Instant.now(), Instant.now()),
                new User(UUID.randomUUID(), "Carol", "King", "carol", "pass", "carol@king.com", "DE", Instant.now(), Instant.now()),
                new User(UUID.randomUUID(), "Bob", "King", "bobk", "pass", "bobk@king.com", "DE", Instant.now(), Instant.now()),
                new User(UUID.randomUUID(), "Alice", "King", "alicek", "pass", "alicek@king.com", "DE", Instant.now(), Instant.now())
        );

        userRepository.saveAll(users);
    }

    @Test
    void getUsers_returnsPaginatedUsers() throws Exception {
        mockMvc.perform(get("/api/users?page=0&size=2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.content[0].firstName").isNotEmpty());
    }

    @Test
    void getUsers_secondPage() throws Exception {
        mockMvc.perform(get("/api/users?page=1&size=2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(5));
    }


    @Test
    void getUsers_secondPage_withCountyFilter() throws Exception {
        mockMvc.perform(get("/api/users?page=1&size=2&country=DE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(3));
    }
}
