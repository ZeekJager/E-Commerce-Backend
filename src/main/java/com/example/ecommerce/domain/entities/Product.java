package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.events.InventoryUpdatedEvent;
import com.example.ecommerce.domain.valueobjects.Money;

import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final String category;
    private Money price;
    private int stock;

    public Product(UUID id, String name, String category, Money price, int stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Product category cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Money getPrice() { return price; }
    public int getStock() { return stock; }

    // Updates stock and returns a domain event for callers that publish events.
    public InventoryUpdatedEvent updateStock(int newStock) {
        this.stock = newStock;
        return new InventoryUpdatedEvent(this.id, newStock);
    }
}
