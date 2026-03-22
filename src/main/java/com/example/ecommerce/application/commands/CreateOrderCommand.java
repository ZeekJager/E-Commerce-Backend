package com.example.ecommerce.application.commands;

import java.util.List;
import java.util.UUID;

public class CreateOrderCommand {
    private final String customerId;
    private final List<UUID> productIds;

    public CreateOrderCommand(String customerId, List<UUID> productIds) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }
        this.customerId = customerId;
        this.productIds = productIds;
    }

    public String getCustomerId() { return customerId; }
    public List<UUID> getProductIds() { return productIds; }
}
