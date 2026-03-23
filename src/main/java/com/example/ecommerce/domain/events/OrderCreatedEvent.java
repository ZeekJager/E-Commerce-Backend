package com.example.ecommerce.domain.events;

import com.example.ecommerce.domain.valueobjects.Address;

import java.util.UUID;

public class OrderCreatedEvent {
    private final UUID orderId;
    private final String customerId;
    private final Address shippingAddress;

    public OrderCreatedEvent(UUID orderId, String customerId, Address shippingAddress) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address cannot be null");
        }
        this.orderId = orderId;
        this.customerId = customerId;
        this.shippingAddress = shippingAddress;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public Address getShippingAddress() { return shippingAddress; }
}
