package com.example.ecommerce.infrastructure.persistence;

import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.repositories.ProductRepository;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.persistence.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaProductRepository implements ProductRepository {
    private final EntityManager entityManager;

    public JpaProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(UUID id) {
        ProductEntity entity = entityManager.find(ProductEntity.class, id);
        if (entity == null) return Optional.empty();
        return Optional.of(mapToDomain(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class)
                .getResultList()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategory(String category) {
        return entityManager.createQuery(
                        "SELECT p FROM ProductEntity p WHERE p.category = :category", ProductEntity.class)
                .setParameter("category", category)
                .getResultList()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Product product) {
        ProductEntity entity = entityManager.find(ProductEntity.class, product.getId());
        if (entity == null) {
            entity = new ProductEntity();
            entity.setId(product.getId());
        }
        entity.setName(product.getName());
        entity.setCategory(product.getCategory());
        entity.setPrice(product.getPrice().amount());
        entity.setCurrency(product.getPrice().currency());
        entity.setStock(product.getStock());
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
                new Money(entity.getPrice(), entity.getCurrency()), entity.getStock());
    }
}
