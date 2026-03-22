package com.example.ecommerce.application.queries;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GetOrderByIdQuery {
    private final UUID orderId;

    public GetOrderByIdQuery(UUID orderId) {
        this.orderId = orderId;
    }
}
