package com.example.ecommerce.application.commands;

import java.util.List;

public class CreateOrderCommand {
    private final String customerId;
    private final List<String> productIds;

    public CreateOrderCommand(String customerId, List<String> productIds) {
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
    public List<String> getProductIds() { return productIds; }
}