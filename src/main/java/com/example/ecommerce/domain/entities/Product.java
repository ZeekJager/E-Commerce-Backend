package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.domain.valueobjects.Quantity;

public class Product {
    private final String id;          // Identity
    private final String name;
    private final String category;
    private Money price;
    private Quantity stock;

    public Product(String id, String name, String category, Money price, Quantity stock) {
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

    // Business behavior
    public void updateStock(Quantity newStock) {
        this.stock = newStock;
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Money getPrice() { return price; }
    public Quantity getStock() { return stock; }
}