package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.UpdateInventoryCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.events.InventoryUpdatedEvent;
import com.example.ecommerce.application.events.handlers.InventoryUpdatedEventHandler;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateInventoryHandler {
    private static final Logger log = LoggerFactory.getLogger(UpdateInventoryHandler.class);

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
            Product product = productRepository.findById(command.getProductId()).orElse(null);
            if (product == null) {
                return Result.failure("Product not found: " + command.getProductId());
            }

            InventoryUpdatedEvent event = product.updateStock(command.getQuantity());
            productRepository.save(product);
            inventoryUpdatedEventHandler.handle(event);

            return Result.success(null);
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error updating inventory for product {}: {}", command.getProductId(), e.getMessage(), e);
            return Result.failure("Failed to update inventory: " + e.getMessage());
        }
    }
}
