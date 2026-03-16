package com.example.ecommerce.api.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ProductRequest {
    private String name;
    private String category;
    private double price;
    private int stock;

    public ProductRequest() {}

    public ProductRequest(String name, String category, double price, int stock) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.price = price;
        this.stock = stock;
    }

}