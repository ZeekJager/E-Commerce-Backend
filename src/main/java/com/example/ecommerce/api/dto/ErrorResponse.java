package com.example.ecommerce.api.dto;

import java.time.Instant;

public class ErrorResponse {
    private final int status;
    private final String error;
    private final Instant timestamp;

    public ErrorResponse(int status, String error) {
        this.status = status;
        this.error = error;
        this.timestamp = Instant.now();
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public Instant getTimestamp() { return timestamp; }
}
