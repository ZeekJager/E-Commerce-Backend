package com.example.ecommerce.api.dto;

import lombok.Getter;

@Getter
public class ProductResponse {
    private final String id;
    private final String name;
    private final String category;
    private final double price;
    private final int stock;

    public ProductResponse(String id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

}