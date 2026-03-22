package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.UpdateInventoryCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.events.InventoryUpdatedEvent;
import com.example.ecommerce.application.events.handlers.InventoryUpdatedEventHandler;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class UpdateInventoryHandler {
    private final InventoryUpdatedEventHandler inventoryUpdatedEventHandler;
    private final ProductRepository productRepository;

    public UpdateInventoryHandler(InventoryUpdatedEventHandler inventoryUpdatedEventHandler,
                                  ProductRepository productRepository) {
        this.inventoryUpdatedEventHandler = inventoryUpdatedEventHandler;
        this.productRepository = productRepository;
    }

    @Transactional
    public Result<Void> handle(UpdateInventoryCommand command) {
        try {
            Product product = productRepository.findById(command.getProductId())
                    .orElse(null);
            if (product == null) {
                return Result.failure("Product not found: " + command.getProductId());
            }

            InventoryUpdatedEvent event = product.updateStock(command.getQuantity());
            productRepository.save(product);
            inventoryUpdatedEventHandler.handle(event);

            return Result.success(null);
        } catch (Exception e) {
            return Result.failure("Failed to update inventory: " + e.getMessage());
        }
    }
}
