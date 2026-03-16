package com.example.ecommerce.domain.events;

public class InventoryUpdatedEvent {
    private final String productId;
    private final int newQuantity;

    public InventoryUpdatedEvent(String productId, int newQuantity) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        }
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    public String getProductId() { return productId; }
    public int getNewQuantity() { return newQuantity; }
}