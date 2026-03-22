package com.example.ecommerce.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private UUID id;
    private String customerId;
    private double totalAmount;

    @ElementCollection
    private List<UUID> productIds;

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<UUID> getProductIds() { return productIds; }
    public void setProductIds(List<UUID> productIds) { this.productIds = productIds; }
}
