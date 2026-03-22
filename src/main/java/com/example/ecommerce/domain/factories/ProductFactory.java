package com.example.ecommerce.domain.factories;

import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.valueobjects.Money;

import java.util.UUID;

public class ProductFactory {
    private ProductFactory() {
    }

    public static Product create(String name, String category, Money price, int stock) {
        return new Product(UUID.randomUUID(), name, category, price, stock);
    }
}
