package com.example.ecommerce.infrastructure.services;

import com.example.ecommerce.application.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void notify(String message) {
        log.info("Notification: {}", message);
        // Integration with push/SMS/in-app alerts here
    }
}
