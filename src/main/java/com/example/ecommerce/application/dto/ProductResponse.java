package com.example.ecommerce.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductResponse {
    private final UUID id;
    private final String name;
    private final String category;
    private final BigDecimal price;
    private final int stock;

    public ProductResponse(UUID id, String name, String category, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}
