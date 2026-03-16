package com.example.ecommerce.infrastructure.repositories;

import com.example.ecommerce.domain.entities.Order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(String id);
    void save(Order order);
    void delete(String id);
}