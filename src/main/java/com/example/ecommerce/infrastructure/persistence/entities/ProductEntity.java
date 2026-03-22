package com.example.ecommerce.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    private UUID id;

    @Version
    private Long version;

    private String name;
    private String category;
    private double price;
    private String currency;
    private int stock;
}
