package com.example.ecommerce.infrastructure.services;

import com.example.ecommerce.application.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendEmail(String recipientId, String message) {
        log.info("Sending email to customer {}: {}", recipientId, message);
        // Integration with SMTP or external provider here
    }
}
