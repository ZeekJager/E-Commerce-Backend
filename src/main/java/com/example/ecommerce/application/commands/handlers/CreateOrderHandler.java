package com.example.ecommerce.application.commands.handlers;

import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.factories.OrderFactory;
import com.example.ecommerce.domain.events.OrderCreatedEvent;
import com.example.ecommerce.application.events.handlers.OrderCreatedEventHandler;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.repositories.OrderRepository;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CreateOrderHandler {
    private final OrderCreatedEventHandler orderCreatedEventHandler;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public CreateOrderHandler(OrderCreatedEventHandler orderCreatedEventHandler,
                              OrderRepository orderRepository,
                              ProductRepository productRepository) {
        this.orderCreatedEventHandler = orderCreatedEventHandler;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Result<UUID> handle(CreateOrderCommand command) {
        try {
            // Fetch all products and validate they exist and have stock
            List<Product> products = new ArrayList<>();
            for (UUID productId : command.getProductIds()) {
                Product product = productRepository.findById(productId)
                        .orElse(null);
                if (product == null) {
                    return Result.failure("Product not found: " + productId);
                }
                if (product.getStock() < 1) {
                    return Result.failure("Product out of stock: " + productId);
                }
                products.add(product);
            }

            // Calculate real total from actual product prices
            BigDecimal total = products.stream()
                    .map(p -> p.getPrice().amount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Money totalAmount = new Money(total, "USD");

            // Create and persist the order
            Order order = OrderFactory.createOrder(command.getCustomerId(), command.getProductIds(), totalAmount);
            orderRepository.save(order);

            // Raise domain event
            OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getCustomerId());
            orderCreatedEventHandler.handle(event);

            return Result.success(order.getId());
        } catch (Exception e) {
            return Result.failure("Failed to create order: " + e.getMessage());
        }
    }
}
