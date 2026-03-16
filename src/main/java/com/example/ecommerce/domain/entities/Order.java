package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.valueobjects.Money;
import java.util.List;

public class Order {
    private final String id;              // Identity
    private final String customerId;
    private final List<String> productIds;
    private final Money totalAmount;

    public Order(String id, String customerId, List<String> productIds, Money totalAmount) {
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
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<String> getProductIds() { return productIds; }
}