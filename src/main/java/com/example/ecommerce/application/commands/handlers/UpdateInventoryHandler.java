package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.UpdateInventoryCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.events.InventoryUpdatedEvent;
import com.example.ecommerce.application.events.handlers.InventoryUpdatedEventHandler;
import com.example.ecommerce.domain.valueobjects.Quantity;
import org.springframework.stereotype.Component;

@Component
public class UpdateInventoryHandler {
    private final InventoryUpdatedEventHandler inventoryUpdatedEventHandler;

    public UpdateInventoryHandler(InventoryUpdatedEventHandler inventoryUpdatedEventHandler) {
        this.inventoryUpdatedEventHandler = inventoryUpdatedEventHandler;
    }

    public Result<Void> handle(UpdateInventoryCommand command) {
        try {
            // Mock product retrieval (in reality, fetch from repository)
            Product product = new Product(command.getProductId(), "Sample", "Category",
                    null, new Quantity(10));

            // Update stock and raise event
            InventoryUpdatedEvent event = product.updateStock(new Quantity(command.getQuantity()));
            inventoryUpdatedEventHandler.handle(event);

            return Result.success(null);
        } catch (Exception e) {
            return Result.failure("Failed to update inventory: " + e.getMessage());
        }
    }
}