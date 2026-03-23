package com.example.ecommerce.domain.aggregates;

import com.example.ecommerce.domain.entities.Order;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.valueobjects.Address;
import com.example.ecommerce.domain.valueobjects.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderAggregateTest {

    private Product product(double price, int stock) {
        return new Product(UUID.randomUUID(), "Item", "Category",
                new Money(BigDecimal.valueOf(price), "USD"), stock);
    }

    private static final Address ADDRESS = new Address("1 Test St", "Testville", "00001", "US");

    private Order order(List<Product> products) {
        BigDecimal total = products.stream()
                .map(p -> p.getPrice().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<UUID> ids = products.stream().map(Product::getId).toList();
        return new Order(UUID.randomUUID(), "cust-1", ids, new Money(total, "USD"), ADDRESS);
    }

    @Test
    void constructor_rejectsEmptyProductList() {
        Order o = order(List.of(product(10, 1)));
        assertThrows(IllegalArgumentException.class, () -> new OrderAggregate(o, List.of()));
    }

    @Test
    void calculateTotalAmount_sumsAllProductPrices() {
        List<Product> products = List.of(product(10.00, 1), product(20.00, 1));
        OrderAggregate aggregate = new OrderAggregate(order(products), products);
        assertEquals(BigDecimal.valueOf(30.00), aggregate.calculateTotalAmount().amount());
    }

    @Test
    void reduceStock_decrementsEachProductByOne() {
        Product p1 = product(10, 5);
        Product p2 = product(20, 3);
        OrderAggregate aggregate = new OrderAggregate(order(List.of(p1, p2)), List.of(p1, p2));
        aggregate.reduceStock();
        assertEquals(4, p1.getStock());
        assertEquals(2, p2.getStock());
    }

    @Test
    void createOrderEvent_returnsEventWithCorrectIds() {
        Product p = product(50, 2);
        Order o = order(List.of(p));
        OrderAggregate aggregate = new OrderAggregate(o, List.of(p));
        var event = aggregate.createOrderEvent();
        assertEquals(o.getId(), event.getOrderId());
        assertEquals(o.getCustomerId(), event.getCustomerId());
    }
}
