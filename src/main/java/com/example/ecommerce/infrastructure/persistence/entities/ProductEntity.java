package com.example.ecommerce.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "products")
public class ProductEntity {
    // Getters and setters
    @Id
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;

    public void setId(String id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setCategory(String category) { this.category = category; }

    public void setPrice(double price) { this.price = price; }

    public void setStock(int stock) { this.stock = stock; }
}