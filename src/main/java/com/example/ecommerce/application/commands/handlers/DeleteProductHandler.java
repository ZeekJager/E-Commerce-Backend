package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.DeleteProductCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.repositories.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteProductHandler {
    private final ProductRepository productRepository;

    public DeleteProductHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Result<Void> handle(DeleteProductCommand command) {
        if (productRepository.findById(command.getProductId()).isEmpty()) {
            return Result.failure("Product not found: " + command.getProductId());
        }
        productRepository.delete(command.getProductId());
        return Result.success(null);
    }
}
