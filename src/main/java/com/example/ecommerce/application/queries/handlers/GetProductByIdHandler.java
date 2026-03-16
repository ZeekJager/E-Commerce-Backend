package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.api.dto.ProductResponse;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class GetProductByIdHandler {
    private final ProductRepository productRepository;

    public GetProductByIdHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Result<ProductResponse> handle(GetProductByIdQuery query) {
        return productRepository.findById(query.getProductId())
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice().amount(),
                        product.getStock().value()
                ))
                .map(Result::success)
                .orElse(Result.failure("Product not found"));
    }
}
