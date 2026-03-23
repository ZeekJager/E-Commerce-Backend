package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.repositories.ProductRepository;
import com.example.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListProductsByCategoryHandlerTest {

    @Mock private ProductRepository productRepository;

    private ListProductsByCategoryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ListProductsByCategoryHandler(productRepository);
    }

    @Test
    void handle_returnsProducts_whenCategoryHasItems() {
        Product p = new Product(UUID.randomUUID(), "A", "Electronics",
                new Money(BigDecimal.TEN, "USD"), 5);
        when(productRepository.findByCategory("Electronics")).thenReturn(List.of(p));

        Result<List<ProductResponse>> result = handler.handle(new ListProductsByCategoryQuery("Electronics"));

        assertTrue(result.isSuccess());
        assertEquals(1, result.getValue().size());
        assertEquals("A", result.getValue().get(0).getName());
    }

    @Test
    void handle_returnsEmptyList_whenCategoryHasNoItems() {
        when(productRepository.findByCategory("Unknown")).thenReturn(List.of());

        Result<List<ProductResponse>> result = handler.handle(new ListProductsByCategoryQuery("Unknown"));

        assertTrue(result.isSuccess());
        assertTrue(result.getValue().isEmpty());
    }
}
