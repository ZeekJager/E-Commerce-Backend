package com.example.ecommerce.infrastructure.persistence;

import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.repositories.OrderRepository;
import com.example.ecommerce.domain.valueobjects.Address;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.persistence.entities.OrderEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaOrderRepository implements OrderRepository {
    private final EntityManager entityManager;

    public JpaOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID id) {
        OrderEntity entity = entityManager.find(OrderEntity.class, id);
        if (entity == null) return Optional.empty();
        return Optional.of(mapToDomain(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return entityManager.createQuery("SELECT o FROM OrderEntity o", OrderEntity.class)
                .getResultList()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Order order) {
        OrderEntity entity = entityManager.find(OrderEntity.class, order.getId());
        if (entity == null) {
            entity = new OrderEntity();
            entity.setId(order.getId());
        }
        entity.setCustomerId(order.getCustomerId());
        entity.setProductIds(new ArrayList<>(order.getProductIds()));
        entity.setTotalAmount(order.getTotalAmount().amount());
        entity.setCurrency(order.getTotalAmount().currency());
        entity.setShippingStreet(order.getShippingAddress().street());
        entity.setShippingCity(order.getShippingAddress().city());
        entity.setShippingZipCode(order.getShippingAddress().zipCode());
        entity.setShippingCountry(order.getShippingAddress().country());
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
        Address shippingAddress = new Address(
                entity.getShippingStreet(),
                entity.getShippingCity(),
                entity.getShippingZipCode(),
                entity.getShippingCountry()
        );
        return new Order(entity.getId(), entity.getCustomerId(), entity.getProductIds(),
                new Money(entity.getTotalAmount(), entity.getCurrency()),
                shippingAddress);
    }
}
