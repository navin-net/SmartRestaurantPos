package com.example.smartrestaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Table(name = "sma_user")
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"password", "role"})
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstname;
    private String lastname;
    private String email;

    @Column(nullable = false)
    private String password;      // BCrypt hash

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "token_version", nullable = false)
    private Integer tokenVersion = 0;

    @Column(length = 3)
    private String status = "ACT";   // ACT = active, INA = inactive
}
