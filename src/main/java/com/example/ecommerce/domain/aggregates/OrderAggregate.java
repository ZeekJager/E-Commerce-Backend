package com.example.ecommerce.domain.aggregates;

import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.events.OrderCreatedEvent;
import com.example.ecommerce.domain.valueobjects.Money;

import java.math.BigDecimal;
import java.util.List;

public class OrderAggregate {
    private final Order order;
    private final List<Product> products;

    public OrderAggregate(Order order, List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }
        this.order = order;
        this.products = products;
    }

    public OrderCreatedEvent createOrderEvent() {
        // Raise domain event when order is created
        return new OrderCreatedEvent(order.getId(), order.getCustomerId());
    }

    // Business behavior: calculate total amount
    public Money calculateTotalAmount() {
        BigDecimal total = products.stream()
                .map(p -> p.getPrice().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Money(total, products.get(0).getPrice().currency());
    }

    // Business behavior: reduce stock after order
    public void reduceStock() {
        for (Product product : products) {
            int newStock = product.getStock() - 1; // simplistic: one unit per product
            product.updateStock(newStock);
        }
    }

    // Getters
    public Order getOrder() { return order; }
    public List<Product> getProducts() { return products; }
}
