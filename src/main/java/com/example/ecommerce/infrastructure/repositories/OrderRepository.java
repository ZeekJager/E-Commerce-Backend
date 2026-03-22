package com.example.ecommerce.infrastructure.repositories;

import com.example.ecommerce.domain.entities.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);
    void save(Order order);
    void delete(UUID id);
}
