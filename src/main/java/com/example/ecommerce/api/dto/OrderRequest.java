package com.example.ecommerce.api.dto;

import java.util.List;
import java.util.Objects;

public class OrderRequest {
    private String customerId;
    private List<String> productIds;

    public OrderRequest() {}

    public OrderRequest(String customerId, List<String> productIds) {
        this.customerId = Objects.requireNonNull(customerId, "Customer ID cannot be null");
        this.productIds = Objects.requireNonNull(productIds, "Product IDs cannot be null");
    }

    public String getCustomerId() { return customerId; }
    public List<String> getProductIds() { return productIds; }
}