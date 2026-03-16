package com.example.ecommerce.infrastructure.repositories;

import com.example.ecommerce.domain.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
    List<Product> findByCategory(String category);
    void save(Product product);
    void delete(String id);
}