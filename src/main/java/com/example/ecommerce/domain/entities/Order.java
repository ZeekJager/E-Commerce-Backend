package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.valueobjects.Money;

import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String customerId;
    private final List<UUID> productIds;
    private final Money totalAmount;

    public Order(UUID id, String customerId, List<UUID> productIds, Money totalAmount) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }
        if (totalAmount.amount() <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        this.id = id;
        this.customerId = customerId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
    }

    // Business behavior
    public Money getTotalAmount() { return totalAmount; }

    // Getters
    public UUID getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<UUID> getProductIds() { return productIds; }
}
