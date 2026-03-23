package com.example.ecommerce.application.commands;

import java.util.UUID;

public class DeleteProductCommand {
    private final UUID productId;

    public DeleteProductCommand(UUID productId) {
        if (productId == null) throw new IllegalArgumentException("Product ID is required");
        this.productId = productId;
    }

    public UUID getProductId() { return productId; }
}
