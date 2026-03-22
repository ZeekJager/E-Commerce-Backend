package com.example.ecommerce.domain.events;

import java.util.UUID;

public class OrderCreatedEvent {
    private final UUID orderId;
    private final String customerId;

    public OrderCreatedEvent(UUID orderId, String customerId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
}
