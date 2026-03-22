package com.example.ecommerce.infrastructure.persistence;

import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.repositories.OrderRepository;

import java.math.BigDecimal;
import com.example.ecommerce.infrastructure.persistence.entities.OrderEntity;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepository implements OrderRepository {
    private final EntityManager entityManager;

    @Autowired
    public JpaOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        OrderEntity entity = entityManager.find(OrderEntity.class, id);
        if (entity == null) return Optional.empty();
        return Optional.of(mapToDomain(entity));
    }

    @Override
    @Transactional
    public void save(Order order) {
        OrderEntity entity = mapToEntity(order);
        entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        OrderEntity entity = entityManager.find(OrderEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    private Order mapToDomain(OrderEntity entity) {
        return new Order(entity.getId(), entity.getCustomerId(),
                entity.getProductIds(), new Money(BigDecimal.valueOf(entity.getTotalAmount()), "USD"));
    }

    private OrderEntity mapToEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCustomerId(order.getCustomerId());
        entity.setProductIds(order.getProductIds());
        entity.setTotalAmount(order.getTotalAmount().amount().doubleValue());
        return entity;
    }
}
