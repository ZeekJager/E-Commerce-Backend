package com.example.ecommerce.infrastructure.repositories;

import com.example.ecommerce.domain.entities.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(UUID id);
    List<Product> findByCategory(String category);
    void save(Product product);
    void delete(UUID id);
}
