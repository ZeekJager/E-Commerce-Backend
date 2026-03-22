package com.example.ecommerce.application.queries;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GetProductByIdQuery {
    private final UUID productId;

    public GetProductByIdQuery(UUID productId) {
        this.productId = productId;
    }
}
