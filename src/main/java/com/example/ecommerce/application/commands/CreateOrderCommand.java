package com.example.ecommerce.application.commands;

import com.example.ecommerce.domain.valueobjects.Address;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class CreateOrderCommand {
    private final String customerId;
    private final List<UUID> productIds;
    private final Address shippingAddress;

    public CreateOrderCommand(String customerId, List<UUID> productIds, Address shippingAddress) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }
        if (new HashSet<>(productIds).size() != productIds.size()) {
            throw new IllegalArgumentException("Order cannot contain duplicate product IDs");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address cannot be null");
        }
        this.customerId = customerId;
        this.productIds = productIds;
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerId() { return customerId; }
    public List<UUID> getProductIds() { return productIds; }
    public Address getShippingAddress() { return shippingAddress; }
}
