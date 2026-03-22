package com.example.ecommerce.infrastructure.persistence;

import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;

import java.math.BigDecimal;
import com.example.ecommerce.infrastructure.persistence.entities.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public class JpaProductRepository implements ProductRepository {
    private final EntityManager entityManager;

    @Autowired
    public JpaProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        ProductEntity entity = entityManager.find(ProductEntity.class, id);
        if (entity == null) return Optional.empty();
        return Optional.of(mapToDomain(entity));
    }

    @Override
    public List<Product> findByCategory(String category) {
        return entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.category = :category", ProductEntity.class)
                .setParameter("category", category)
                .getResultList()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Product product) {
        ProductEntity entity = mapToEntity(product);
        entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        ProductEntity entity = entityManager.find(ProductEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    private Product mapToDomain(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getCategory(),
                new Money(BigDecimal.valueOf(entity.getPrice()), "USD"), entity.getStock());
    }

    private ProductEntity mapToEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice().amount().doubleValue());
        entity.setStock(product.getStock());
        return entity;
    }
}
