package com.example.ecommerce.application.commands;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateInventoryCommand {
    private final UUID productId;
    private final int quantity;

    public UpdateInventoryCommand(UUID productId, int quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.productId = productId;
        this.quantity = quantity;
    }
}
