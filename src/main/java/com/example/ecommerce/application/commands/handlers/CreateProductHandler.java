package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.CreateProductCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.factories.ProductFactory;
import com.example.ecommerce.domain.repositories.ProductRepository;
import com.example.ecommerce.domain.valueobjects.Money;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateProductHandler {
    private final ProductRepository productRepository;

    public CreateProductHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Result<ProductResponse> handle(CreateProductCommand command) {
        try {
            Product product = ProductFactory.create(
                    command.getName(),
                    command.getCategory(),
                    new Money(command.getPrice(), "USD"),
                    command.getStock()
            );
            productRepository.save(product);
            return Result.success(toResponse(product));
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice().amount(),
                product.getStock()
        );
    }
}
