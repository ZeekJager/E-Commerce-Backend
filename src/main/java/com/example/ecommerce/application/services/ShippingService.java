package com.example.ecommerce.application.services;

public interface ShippingService {
    void bookShipment(String orderId, String address);
}