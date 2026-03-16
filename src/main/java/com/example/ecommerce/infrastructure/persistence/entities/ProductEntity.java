package com.example.ecommerce.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
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

}