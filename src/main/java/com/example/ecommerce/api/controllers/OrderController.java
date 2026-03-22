package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ErrorResponse;
import com.example.ecommerce.api.dto.OrderResponse;
import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.commands.handlers.CreateOrderHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.infrastructure.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderCommand command) {
        Result<UUID> result = createOrderHandler.handle(command);
        if (result.isSuccess()) {
            return orderRepository.findById(result.getValue())
                    .map(order -> ResponseEntity.status(HttpStatus.CREATED).body((Object) new OrderResponse(
                            order.getId(),
                            order.getCustomerId(),
                            order.getProductIds(),
                            order.getTotalAmount().amount()
                    )))
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Order created but could not be retrieved")));
        }

        String error = result.getError();
        if (error.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), error));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error));
    }
}
