package com.example.ecommerce.api.dto;

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

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
}