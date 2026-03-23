package com.example.ecommerce.infrastructure.persistence;

import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.valueobjects.Money;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepository repository;

    @Autowired
    private EntityManager entityManager;

    private Product save(String name, String category, double price, int stock) {
        Product p = new Product(UUID.randomUUID(), name, category,
                new Money(BigDecimal.valueOf(price), "USD"), stock);
        repository.save(p);
        entityManager.flush();
        entityManager.clear();
        return p;
    }

    @Test
    void save_and_findById_roundTrip() {
        Product product = save("Widget", "Electronics", 19.99, 5);

        Optional<Product> found = repository.findById(product.getId());

        assertTrue(found.isPresent());
        assertEquals("Widget", found.get().getName());
        assertEquals("Electronics", found.get().getCategory());
        assertEquals(0, BigDecimal.valueOf(19.99).compareTo(found.get().getPrice().amount()));
        assertEquals(5, found.get().getStock());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void findAll_returnsAllSavedProducts() {
        save("A", "Cat1", 10, 1);
        save("B", "Cat2", 20, 2);

        List<Product> all = repository.findAll();

        assertTrue(all.size() >= 2);
    }

    @Test
    void findByCategory_returnsMatchingProducts() {
        save("Item1", "Books", 9.99, 10);
        save("Item2", "Books", 14.99, 5);
        save("Other", "Electronics", 99.99, 1);

        List<Product> books = repository.findByCategory("Books");

        assertEquals(2, books.size());
        assertTrue(books.stream().allMatch(p -> p.getCategory().equals("Books")));
    }

    @Test
    void delete_removesProduct() {
        Product product = save("ToDelete", "Test", 5, 1);

        repository.delete(product.getId());
        entityManager.flush();
        entityManager.clear();

        assertTrue(repository.findById(product.getId()).isEmpty());
    }

    @Test
    void save_updatesExistingProduct() {
        Product original = save("Original", "Cat", 10, 5);

        Product updated = new Product(original.getId(), "Updated", "Cat",
                new Money(BigDecimal.valueOf(20), "USD"), 10);
        repository.save(updated);
        entityManager.flush();
        entityManager.clear();

        Product found = repository.findById(original.getId()).orElseThrow();
        assertEquals("Updated", found.getName());
        assertEquals(10, found.getStock());
    }
}
