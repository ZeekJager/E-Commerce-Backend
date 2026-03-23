package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.valueobjects.Address;
import com.example.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private static final Address ADDRESS = new Address("123 Main St", "Springfield", "12345", "US");

    @Test
    void constructor_rejectsEmptyProductList() {
        assertThrows(IllegalArgumentException.class, () ->
                new Order(UUID.randomUUID(), "cust-1", List.of(),
                        new Money(BigDecimal.valueOf(100), "USD"), ADDRESS));
    }

    @Test
    void constructor_rejectsZeroTotal() {
        assertThrows(IllegalArgumentException.class, () ->
                new Order(UUID.randomUUID(), "cust-1", List.of(UUID.randomUUID()),
                        new Money(BigDecimal.ZERO, "USD"), ADDRESS));
    }

    @Test
    void constructor_rejectsNullShippingAddress() {
        assertThrows(IllegalArgumentException.class, () ->
                new Order(UUID.randomUUID(), "cust-1", List.of(UUID.randomUUID()),
                        new Money(BigDecimal.valueOf(50), "USD"), null));
    }

    @Test
    void constructor_assignsGeneratedId() {
        UUID id = UUID.randomUUID();
        Order order = new Order(id, "cust-1", List.of(UUID.randomUUID()),
                new Money(BigDecimal.valueOf(50), "USD"), ADDRESS);
        assertEquals(id, order.getId());
    }
}
