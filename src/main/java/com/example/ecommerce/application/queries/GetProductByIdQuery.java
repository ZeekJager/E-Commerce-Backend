package com.example.ecommerce.application.queries;

public class GetProductByIdQuery {
    private final String productId;

    public GetProductByIdQuery(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
