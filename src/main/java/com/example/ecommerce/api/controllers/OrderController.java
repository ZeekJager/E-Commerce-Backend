package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ErrorResponse;
import com.example.ecommerce.api.dto.OrderRequest;
import com.example.ecommerce.application.commands.CreateOrderCommand;
import com.example.ecommerce.application.commands.handlers.CreateOrderHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.OrderResponse;
import com.example.ecommerce.application.queries.GetOrderByIdQuery;
import com.example.ecommerce.application.queries.ListAllOrdersQuery;
import com.example.ecommerce.application.queries.handlers.GetOrderByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListAllOrdersHandler;
import com.example.ecommerce.domain.valueobjects.Address;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderHandler createOrderHandler;
    private final GetOrderByIdHandler getOrderByIdHandler;
    private final ListAllOrdersHandler listAllOrdersHandler;

    public OrderController(CreateOrderHandler createOrderHandler,
                           GetOrderByIdHandler getOrderByIdHandler,
                           ListAllOrdersHandler listAllOrdersHandler) {
        this.createOrderHandler = createOrderHandler;
        this.getOrderByIdHandler = getOrderByIdHandler;
        this.listAllOrdersHandler = listAllOrdersHandler;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request) {
        Address shippingAddress = new Address(
                request.getShippingAddress().getStreet(),
                request.getShippingAddress().getCity(),
                request.getShippingAddress().getZipCode(),
                request.getShippingAddress().getCountry()
        );
        CreateOrderCommand command = new CreateOrderCommand(
                request.getCustomerId(), request.getProductIds(), shippingAddress);

        Result<UUID> result = createOrderHandler.handle(command);
        if (!result.isSuccess()) {
            String error = result.getError();
            HttpStatus status = error.contains("not found") ? HttpStatus.NOT_FOUND
                    : error.contains("out of stock") ? HttpStatus.CONFLICT
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(new ErrorResponse(status.value(), error));
        }
        UUID orderId = result.getValue();
        Result<OrderResponse> orderResult = getOrderByIdHandler.handle(new GetOrderByIdQuery(orderId));
        if (!orderResult.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Order created but could not be retrieved"));
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(orderId).toUri();
        return ResponseEntity.created(location).body(orderResult.getValue());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listAllOrders() {
        return ResponseEntity.ok(listAllOrdersHandler.handle(new ListAllOrdersQuery()).getValue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable UUID id) {
        Result<OrderResponse> result = getOrderByIdHandler.handle(new GetOrderByIdQuery(id));
        if (result.isSuccess()) return ResponseEntity.ok(result.getValue());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
    }
}
