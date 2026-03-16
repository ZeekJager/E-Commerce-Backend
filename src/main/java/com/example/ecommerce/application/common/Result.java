package com.example.ecommerce.application.common;

public class Result<T> {
    private final T value;
    private final String errorMessage;
    private final boolean success;

    private Result(T value, String errorMessage, boolean success) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    public boolean isSuccess() { return success; }
    public T getValue() { return value; }
    public String getErrorMessage() { return errorMessage; }
}
