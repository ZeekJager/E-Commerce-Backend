package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.OrderResponse;
import com.example.ecommerce.application.queries.GetOrderByIdQuery;
import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.repositories.OrderRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderByIdHandlerTest {

    @Mock private OrderRepository orderRepository;

    private GetOrderByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetOrderByIdHandler(orderRepository);
    }

    @Test
    void handle_returnsOrder_whenFound() {
        UUID orderId = UUID.randomUUID();
        Address address = new Address("1 St", "City", "00001", "US");
        Order order = new Order(orderId, "cust-1", List.of(UUID.randomUUID()),
                new Money(BigDecimal.valueOf(50), "USD"), address);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Result<OrderResponse> result = handler.handle(new GetOrderByIdQuery(orderId));

        assertTrue(result.isSuccess());
        assertEquals(orderId, result.getValue().getId());
        assertEquals("cust-1", result.getValue().getCustomerId());
        assertTrue(result.getValue().getShippingAddress().contains("1 St"));
    }

    @Test
    void handle_returnsFailure_whenNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Result<OrderResponse> result = handler.handle(new GetOrderByIdQuery(orderId));

        assertTrue(result.isFailure());
        assertTrue(result.getError().contains(orderId.toString()));
    }
}
