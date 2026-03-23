package com.example.ecommerce.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateProductCommand {
    private final UUID productId;
    private final String name;
    private final String category;
    private final BigDecimal price;
    private final int stock;

    public UpdateProductCommand(UUID productId, String name, String category, BigDecimal price, int stock) {
        if (productId == null) throw new IllegalArgumentException("Product ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Category is required");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Price must be positive");
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public UUID getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}
