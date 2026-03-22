package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.dto.OrderResponse;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.queries.GetOrderByIdQuery;
import com.example.ecommerce.domain.valueobjects.Address;
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
                .map(order -> {
                    Address addr = order.getShippingAddress();
                    String formattedAddress = addr.street() + ", " + addr.city()
                            + ", " + addr.zipCode() + ", " + addr.country();
                    return new OrderResponse(
                            order.getId(),
                            order.getCustomerId(),
                            order.getProductIds(),
                            order.getTotalAmount().amount(),
                            formattedAddress
                    );
                })
                .map(Result::success)
                .orElse(Result.failure("Order not found: " + query.getOrderId()));
    }
}
