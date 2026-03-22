package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.api.dto.OrderResponse;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.queries.GetOrderByIdQuery;
import com.example.ecommerce.infrastructure.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class GetOrderByIdHandler {
    private final OrderRepository orderRepository;

    public GetOrderByIdHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Result<OrderResponse> handle(GetOrderByIdQuery query) {
        return orderRepository.findById(query.getOrderId())
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getCustomerId(),
                        order.getProductIds(),
                        order.getTotalAmount().amount()
                ))
                .map(Result::success)
                .orElse(Result.failure("Order not found: " + query.getOrderId()));
    }
}
