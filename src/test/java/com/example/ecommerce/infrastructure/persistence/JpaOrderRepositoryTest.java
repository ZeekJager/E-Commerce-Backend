package com.example.ecommerce.infrastructure.persistence;

import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.valueobjects.Address;
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
class JpaOrderRepositoryTest {

    @Autowired
    private JpaOrderRepository repository;

    @Autowired
    private EntityManager entityManager;

    private static final Address ADDRESS = new Address("10 Oak Ave", "Townsville", "99999", "US");

    private Order save(String customerId, double amount) {
        Order order = new Order(UUID.randomUUID(), customerId, List.of(UUID.randomUUID()),
                new Money(BigDecimal.valueOf(amount), "USD"), ADDRESS);
        repository.save(order);
        entityManager.flush();
        entityManager.clear();
        return order;
    }

    @Test
    void save_and_findById_roundTrip() {
        Order order = save("cust-1", 99.99);

        Optional<Order> found = repository.findById(order.getId());

        assertTrue(found.isPresent());
        assertEquals("cust-1", found.get().getCustomerId());
        assertEquals(0, BigDecimal.valueOf(99.99).compareTo(found.get().getTotalAmount().amount()));
        assertEquals("10 Oak Ave", found.get().getShippingAddress().street());
        assertEquals("Townsville", found.get().getShippingAddress().city());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        assertTrue(repository.findById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void findAll_returnsAllSavedOrders() {
        save("cust-1", 10);
        save("cust-2", 20);

        List<Order> all = repository.findAll();

        assertTrue(all.size() >= 2);
    }

    @Test
    void delete_removesOrder() {
        Order order = save("cust-del", 50);

        repository.delete(order.getId());
        entityManager.flush();
        entityManager.clear();

        assertTrue(repository.findById(order.getId()).isEmpty());
    }

    @Test
    void save_updatesExistingOrder() {
        Order original = save("cust-1", 100);
        Address newAddress = new Address("99 New Rd", "Newtown", "11111", "US");
        Order updated = new Order(original.getId(), "cust-1", List.of(UUID.randomUUID()),
                new Money(BigDecimal.valueOf(200), "USD"), newAddress);

        repository.save(updated);
        entityManager.flush();
        entityManager.clear();

        Order found = repository.findById(original.getId()).orElseThrow();
        assertEquals(0, BigDecimal.valueOf(200).compareTo(found.getTotalAmount().amount()));
        assertEquals("99 New Rd", found.getShippingAddress().street());
    }
}
