package com.example.ecommerce.api.controllers;

import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.commands.handlers.CreateOrderHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.api.dto.OrderResponse;
import com.example.ecommerce.infrastructure.repositories.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderHandler createOrderHandler;
    private final OrderRepository orderRepository;

    public OrderController(CreateOrderHandler createOrderHandler, OrderRepository orderRepository) {
        this.createOrderHandler = createOrderHandler;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Result<OrderResponse> createOrder(@RequestBody CreateOrderCommand command) {
        Result<UUID> result = createOrderHandler.handle(command);
        if (result.isSuccess()) {
            return orderRepository.findById(result.getValue())
                    .map(order -> Result.success(new OrderResponse(
                            order.getId(),
                            order.getCustomerId(),
                            order.getProductIds(),
                            order.getTotalAmount().amount()
                    )))
                    .orElse(Result.failure("Order created but could not be retrieved"));
        }
        return Result.failure(result.getError());
    }
}
