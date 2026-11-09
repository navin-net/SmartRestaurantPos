package com.example.smartrestaurant.service;


import com.example.smartrestaurant.dto.request.ProductRequest;
import com.example.smartrestaurant.model.Products;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Products> findAll();

    Products findById(Integer id);

    Products create(ProductRequest request);

    Products update(ProductRequest request);

    void delete(Integer id);

}
