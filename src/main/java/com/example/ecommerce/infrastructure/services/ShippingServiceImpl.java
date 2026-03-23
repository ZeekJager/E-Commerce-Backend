package com.example.ecommerce.infrastructure.services;

import com.example.ecommerce.application.services.ShippingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements ShippingService {
    private static final Logger log = LoggerFactory.getLogger(ShippingServiceImpl.class);

    @Override
    public void bookShipment(String orderId, String address) {
        log.info("Booking shipment for order {} to address: {}", orderId, address);
        // Integration with logistics provider here
    }
}
