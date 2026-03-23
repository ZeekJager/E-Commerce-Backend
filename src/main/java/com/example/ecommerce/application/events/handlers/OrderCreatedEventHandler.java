package com.example.ecommerce.application.events.handlers;

import com.example.ecommerce.application.services.NotificationService;
import com.example.ecommerce.application.services.ShippingService;
import com.example.ecommerce.domain.events.OrderCreatedEvent;
import com.example.ecommerce.domain.valueobjects.Address;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventHandler {
    private final NotificationService notificationService;
    private final ShippingService shippingService;

    public OrderCreatedEventHandler(NotificationService notificationService,
                                    ShippingService shippingService) {
        this.notificationService = notificationService;
        this.shippingService = shippingService;
    }

    public void handle(OrderCreatedEvent event) {
        String orderId = event.getOrderId().toString();
        String customerId = event.getCustomerId();
        Address address = event.getShippingAddress();
        String formattedAddress = address.street() + ", " + address.city()
                + ", " + address.zipCode() + ", " + address.country();

        notificationService.notify("Order " + orderId + " created for customer " + customerId);
        shippingService.bookShipment(orderId, formattedAddress);
        // TODO: send order confirmation email once a customer lookup service exists
    }
}
