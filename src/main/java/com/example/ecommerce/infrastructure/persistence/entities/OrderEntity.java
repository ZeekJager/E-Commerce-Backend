package com.example.ecommerce.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private UUID id;

    @Version
    private Long version;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @ElementCollection
    @CollectionTable(name = "order_product_ids", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "product_id", nullable = false)
    private List<UUID> productIds;

    @Column(nullable = false)
    private String shippingStreet;

    @Column(nullable = false)
    private String shippingCity;

    @Column(nullable = false)
    private String shippingZipCode;

    @Column(nullable = false)
    private String shippingCountry;
}
