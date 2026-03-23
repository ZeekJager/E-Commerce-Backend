package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.OrderResponse;
import com.example.ecommerce.application.queries.ListAllOrdersQuery;
import com.example.ecommerce.domain.repositories.OrderRepository;
import com.example.ecommerce.domain.valueobjects.Address;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListAllOrdersHandler {
    private final OrderRepository orderRepository;

    public ListAllOrdersHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public Result<List<OrderResponse>> handle(ListAllOrdersQuery query) {
        List<OrderResponse> orders = orderRepository.findAll().stream()
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
                .collect(Collectors.toList());
        return Result.success(orders);
    }
}
