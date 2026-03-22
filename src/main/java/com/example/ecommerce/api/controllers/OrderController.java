package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ErrorResponse;
import com.example.ecommerce.api.dto.OrderResponse;
import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.commands.handlers.CreateOrderHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.queries.GetOrderByIdQuery;
import com.example.ecommerce.application.queries.handlers.GetOrderByIdHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderHandler createOrderHandler;
    private final GetOrderByIdHandler getOrderByIdHandler;

    public OrderController(CreateOrderHandler createOrderHandler,
                           GetOrderByIdHandler getOrderByIdHandler) {
        this.createOrderHandler = createOrderHandler;
        this.getOrderByIdHandler = getOrderByIdHandler;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderCommand command) {
        Result<UUID> result = createOrderHandler.handle(command);
        if (result.isSuccess()) {
            Result<OrderResponse> orderResult = getOrderByIdHandler.handle(new GetOrderByIdQuery(result.getValue()));
            if (orderResult.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(orderResult.getValue());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Order created but could not be retrieved"));
        }

        String error = result.getError();
        if (error.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), error));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), error));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        Result<OrderResponse> result = getOrderByIdHandler.handle(new GetOrderByIdQuery(id));
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
    }
}
