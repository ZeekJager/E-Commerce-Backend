package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.events.InventoryUpdatedEvent;
import com.example.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product validProduct() {
        return new Product(UUID.randomUUID(), "Widget", "Electronics",
                new Money(BigDecimal.valueOf(99.99), "USD"), 10);
    }

    @Test
    void constructor_rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product(UUID.randomUUID(), "", "Electronics",
                        new Money(BigDecimal.TEN, "USD"), 5));
    }

    @Test
    void constructor_rejectsBlankCategory() {
        assertThrows(IllegalArgumentException.class, () ->
                new Product(UUID.randomUUID(), "Widget", "",
                        new Money(BigDecimal.TEN, "USD"), 5));
    }

    @Test
    void updateStock_returnsInventoryUpdatedEvent() {
        Product product = validProduct();
        InventoryUpdatedEvent event = product.updateStock(5);
        assertEquals(5, event.getNewQuantity());
        assertEquals(product.getId(), event.getProductId());
    }

    @Test
    void updateStock_updatesStockValue() {
        Product product = validProduct();
        product.updateStock(3);
        assertEquals(3, product.getStock());
    }

    @Test
    void updateStock_rejectsNegativeStock() {
        Product product = validProduct();
        assertThrows(IllegalArgumentException.class, () -> product.updateStock(-1));
    }

    @Test
    void updatePrice_updatesPrice() {
        Product product = validProduct();
        Money newPrice = new Money(BigDecimal.valueOf(149.99), "USD");
        product.updatePrice(newPrice);
        assertEquals(newPrice, product.getPrice());
    }
}
