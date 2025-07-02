package com.faceit.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String nickname;

    private String password;

    private String email;

    private String country;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getFirstName() {
//        return this.firstName;
//    }
}
