package com.example.ecommerce.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class OrderRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotEmpty(message = "Order must contain at least one product")
    private List<UUID> productIds;

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressRequest shippingAddress;

    public OrderRequest() {}

    public String getCustomerId() { return customerId; }
    public List<UUID> getProductIds() { return productIds; }
    public AddressRequest getShippingAddress() { return shippingAddress; }
}
