package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.UpdateProductCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.repositories.ProductRepository;
import com.example.ecommerce.domain.valueobjects.Money;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateProductHandler {
    private final ProductRepository productRepository;

    public UpdateProductHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Result<ProductResponse> handle(UpdateProductCommand command) {
        if (productRepository.findById(command.getProductId()).isEmpty()) {
            return Result.failure("Product not found: " + command.getProductId());
        }
        try {
            Product updated = new Product(
                    command.getProductId(),
                    command.getName(),
                    command.getCategory(),
                    new Money(command.getPrice(), "USD"),
                    command.getStock()
            );
            productRepository.save(updated);
            return Result.success(new ProductResponse(
                    updated.getId(),
                    updated.getName(),
                    updated.getCategory(),
                    updated.getPrice().amount(),
                    updated.getStock()
            ));
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}
