package com.example.ecommerce.infrastructure.services;

import com.example.ecommerce.application.services.ShippingService;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {
    @Override
    public void bookShipment(String orderId, String address) {
        System.out.println("Booking shipment for order " + orderId + " to address: " + address);
        // Integration with logistics provider here
    }
}