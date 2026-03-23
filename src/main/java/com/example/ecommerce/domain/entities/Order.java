package com.example.ecommerce.domain.entities;

import com.example.ecommerce.domain.valueobjects.Address;
import com.example.ecommerce.domain.valueobjects.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final String customerId;
    private final List<UUID> productIds;
    private final Money totalAmount;
    private final Address shippingAddress;

    public Order(UUID id, String customerId, List<UUID> productIds, Money totalAmount, Address shippingAddress) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }
        if (totalAmount.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address cannot be null");
        }
        this.id = id;
        this.customerId = customerId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
    }

    public UUID getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<UUID> getProductIds() { return productIds; }
    public Money getTotalAmount() { return totalAmount; }
    public Address getShippingAddress() { return shippingAddress; }
}
