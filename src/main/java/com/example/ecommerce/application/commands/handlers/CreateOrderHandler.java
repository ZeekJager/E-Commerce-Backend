package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.factories.OrderFactory;
import com.example.ecommerce.domain.events.OrderCreatedEvent;
import com.example.ecommerce.application.events.handlers.OrderCreatedEventHandler;
import com.example.ecommerce.domain.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateOrderHandler {
    private final OrderCreatedEventHandler orderCreatedEventHandler;

    public CreateOrderHandler(OrderCreatedEventHandler orderCreatedEventHandler) {
        this.orderCreatedEventHandler = orderCreatedEventHandler;
    }

    public Result<String> handle(CreateOrderCommand command) {
        try {
            // Calculate total amount (simplified: assume fixed price per product)
            Money totalAmount = new Money(100.0 * command.getProductIds().size(), "USD");

            // Create order via factory
            Order order = OrderFactory.createOrder(command.getCustomerId(), command.getProductIds(), totalAmount);

            // Raise domain event
            OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getCustomerId());
            orderCreatedEventHandler.handle(event);

            return Result.success(order.getId());
        } catch (Exception e) {
            return Result.failure("Failed to create order: " + e.getMessage());
        }
    }
}