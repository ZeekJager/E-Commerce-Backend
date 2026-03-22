package com.example.ecommerce.domain.repositories;

import com.example.ecommerce.domain.entities.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Optional<Order> findById(UUID id);
    List<Order> findAll();
    void save(Order order);
    void delete(UUID id);
}
