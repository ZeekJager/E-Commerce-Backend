package com.example.ecommerce.application.commands;

import com.example.ecommerce.domain.valueobjects.Address;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderCommandTest {

    private static final Address ADDRESS = new Address("1 Main St", "City", "12345", "US");

    @Test
    void constructor_rejectsNullCustomerId() {
        assertThrows(IllegalArgumentException.class, () ->
                new CreateOrderCommand(null, List.of(UUID.randomUUID()), ADDRESS));
    }

    @Test
    void constructor_rejectsBlankCustomerId() {
        assertThrows(IllegalArgumentException.class, () ->
                new CreateOrderCommand("  ", List.of(UUID.randomUUID()), ADDRESS));
    }

    @Test
    void constructor_rejectsEmptyProductList() {
        assertThrows(IllegalArgumentException.class, () ->
                new CreateOrderCommand("cust-1", List.of(), ADDRESS));
    }

    @Test
    void constructor_rejectsDuplicateProductIds() {
        UUID id = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () ->
                new CreateOrderCommand("cust-1", List.of(id, id), ADDRESS));
    }

    @Test
    void constructor_rejectsNullShippingAddress() {
        assertThrows(IllegalArgumentException.class, () ->
                new CreateOrderCommand("cust-1", List.of(UUID.randomUUID()), null));
    }

    @Test
    void constructor_acceptsValidInputs() {
        UUID id = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand("cust-1", List.of(id), ADDRESS);
        assertEquals("cust-1", cmd.getCustomerId());
        assertEquals(List.of(id), cmd.getProductIds());
        assertEquals(ADDRESS, cmd.getShippingAddress());
    }
}
