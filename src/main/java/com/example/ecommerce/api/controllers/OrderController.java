package com.example.ecommerce.api.controllers;

import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.commands.handlers.CreateOrderHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.api.dto.OrderResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderHandler createOrderHandler;

    public OrderController(CreateOrderHandler createOrderHandler) {
        this.createOrderHandler = createOrderHandler;
    }

    @PostMapping
    public Result<OrderResponse> createOrder(@RequestBody CreateOrderCommand command) {
        Result<String> result = createOrderHandler.handle(command);
        if (result.isSuccess()) {
            return Result.success(new OrderResponse(
                    result.getValue(),
                    command.getCustomerId(),
                    command.getProductIds(),
                    100.0 * command.getProductIds().size() // simplified total
            ));
        }
        return Result.failure(result.getError());
    }
}