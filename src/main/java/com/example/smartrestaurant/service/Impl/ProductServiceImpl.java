package com.example.smartrestaurant.service.Impl;

import com.example.smartrestaurant.dto.request.ProductRequest;
import com.example.smartrestaurant.model.Products;
import com.example.smartrestaurant.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    @Override
    public List<Products> findAll() {
        return List.of();
    }

    @Override
    public Products findById(Integer id) {
        return null;
    }

    @Override
    public Products create(ProductRequest request) {
        return null;
    }

    @Override
    public Products update(ProductRequest request) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
