package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.events.InventoryUpdatedEvent;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.domain.valueobjects.Quantity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class Product {
    private final UUID id;          // Identity
    private final String name;
    private final String category; // make it class
    private Money price;
    private Quantity stock; // don't make it class

    public Product(UUID id, String name, String category, Money price, Quantity stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Product category cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public void updatePrice(Money newPrice) {
        this.price = newPrice;
    }



    // Updates stock and returns a domain event for callers that publish events.
    public InventoryUpdatedEvent updateStock(Quantity newStock) {
        this.stock = newStock;
        return new InventoryUpdatedEvent(this.id, newStock.value());
    }
}
