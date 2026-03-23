package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.application.queries.GetProductByIdQuery;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductByIdHandlerTest {

    @Mock private ProductRepository productRepository;

    private GetProductByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetProductByIdHandler(productRepository);
    }

    @Test
    void handle_returnsProduct_whenFound() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Widget", "Electronics",
                new Money(BigDecimal.valueOf(49.99), "USD"), 10);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Result<ProductResponse> result = handler.handle(new GetProductByIdQuery(id));

        assertTrue(result.isSuccess());
        assertEquals(id, result.getValue().getId());
        assertEquals("Widget", result.getValue().getName());
        assertEquals(BigDecimal.valueOf(49.99), result.getValue().getPrice());
    }

    @Test
    void handle_returnsFailure_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Result<ProductResponse> result = handler.handle(new GetProductByIdQuery(id));

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains(id.toString()));
    }
}
