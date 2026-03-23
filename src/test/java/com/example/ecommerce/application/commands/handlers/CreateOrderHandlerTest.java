package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.events.handlers.OrderCreatedEventHandler;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.repositories.OrderRepository;
import com.example.ecommerce.domain.repositories.ProductRepository;
import com.example.ecommerce.domain.valueobjects.Address;
import com.example.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderHandlerTest {

    @Mock private OrderCreatedEventHandler eventHandler;
    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;

    private CreateOrderHandler handler;

    private static final Address ADDRESS = new Address("1 Main St", "City", "12345", "US");

    @BeforeEach
    void setUp() {
        handler = new CreateOrderHandler(eventHandler, orderRepository, productRepository);
    }

    @Test
    void handle_returnsFailure_whenProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Result<UUID> result = handler.handle(
                new CreateOrderCommand("cust-1", List.of(productId), ADDRESS));

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("not found"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void handle_returnsFailure_whenProductOutOfStock() {
        UUID productId = UUID.randomUUID();
        Product outOfStock = new Product(productId, "Item", "Cat",
                new Money(BigDecimal.TEN, "USD"), 0);
        when(productRepository.findById(productId)).thenReturn(Optional.of(outOfStock));

        Result<UUID> result = handler.handle(
                new CreateOrderCommand("cust-1", List.of(productId), ADDRESS));

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains("out of stock"));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void handle_savesOrderAndReducesStock_onSuccess() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Item", "Cat",
                new Money(BigDecimal.valueOf(25), "USD"), 3);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Result<UUID> result = handler.handle(
                new CreateOrderCommand("cust-1", List.of(productId), ADDRESS));

        assertTrue(result.isSuccess());
        assertNotNull(result.getValue());
        verify(productRepository).save(product);
        verify(orderRepository).save(any());
        verify(eventHandler).handle(any());
        assertEquals(2, product.getStock()); // stock reduced by 1
    }

    @Test
    void handle_returnsFailure_whenIllegalArgumentThrown() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Item", "Cat",
                new Money(BigDecimal.valueOf(10), "USD"), 5);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doThrow(new IllegalArgumentException("bad input")).when(orderRepository).save(any());

        Result<UUID> result = handler.handle(
                new CreateOrderCommand("cust-1", List.of(productId), ADDRESS));

        assertTrue(result.isFailure());
        assertEquals("bad input", result.getError());
    }
}
