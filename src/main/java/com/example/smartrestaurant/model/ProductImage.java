package com.example.smartrestaurant.model;

import com.example.smartrestaurant.model.response.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sma_product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductImages extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products products;
    @Column(name = "sub_image")
    private String subImage;

}
