package com.example.ecommerce.domain.events;

import java.util.UUID;

public class InventoryUpdatedEvent {
    private final UUID productId;
    private final int newQuantity;

    public InventoryUpdatedEvent(UUID productId, int newQuantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    public UUID getProductId() { return productId; }
    public int getNewQuantity() { return newQuantity; }
}
