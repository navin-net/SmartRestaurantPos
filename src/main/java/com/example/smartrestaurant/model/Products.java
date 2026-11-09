package com.example.smartrestaurant.model;

import com.example.smartrestaurant.model.response.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sma_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Products extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "available")
    private String available;

    @Column(name = "code")
    private String code;

    @Column(name = "cost_price", nullable = false)
    private Double costPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "name")
    private String name;

    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

}
