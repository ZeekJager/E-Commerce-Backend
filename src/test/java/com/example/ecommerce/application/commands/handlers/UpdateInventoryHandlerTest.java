package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.UpdateInventoryCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.events.handlers.InventoryUpdatedEventHandler;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.repositories.ProductRepository;
import com.example.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateInventoryHandlerTest {

    @Mock private InventoryUpdatedEventHandler eventHandler;
    @Mock private ProductRepository productRepository;

    private UpdateInventoryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateInventoryHandler(eventHandler, productRepository);
    }

    @Test
    void handle_returnsFailure_whenProductNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Result<Void> result = handler.handle(new UpdateInventoryCommand(id, 10));

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("not found"));
        verify(productRepository, never()).save(any());
    }

    @Test
    void handle_updatesStockAndFiresEvent_onSuccess() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Item", "Cat",
                new Money(BigDecimal.TEN, "USD"), 5);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Result<Void> result = handler.handle(new UpdateInventoryCommand(id, 15));

        assertTrue(result.isSuccess());
        assertEquals(15, product.getStock());
        verify(productRepository).save(product);
        verify(eventHandler).handle(any());
    }
}
