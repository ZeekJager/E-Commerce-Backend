package com.example.ecommerce.api.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
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
}
