package com.example.ecommerce.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderResponse {
    private final UUID id;
    private final String customerId;
    private final List<UUID> productIds;
    private final BigDecimal totalAmount;

    public OrderResponse(UUID id, String customerId, List<UUID> productIds, BigDecimal totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
    }

    public UUID getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<UUID> getProductIds() { return productIds; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
