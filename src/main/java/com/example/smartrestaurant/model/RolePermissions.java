package com.example.smartrestaurant.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sma_role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Permissions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int role_id;
    private int permission_id;
}
