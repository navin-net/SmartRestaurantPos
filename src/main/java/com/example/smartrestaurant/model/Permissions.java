package com.example.smartrestaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sma_permissions")
@Getter @Setter @NoArgsConstructor
public class Permissions {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;          // e.g. role:view, product:delete

    private String description;
}
