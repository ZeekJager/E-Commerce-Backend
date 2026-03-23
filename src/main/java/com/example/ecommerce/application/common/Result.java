package com.example.ecommerce.application.common;

/**
 * Minimal functional-style result wrapper used across controllers/handlers.
 */
public final class Result<T> {
	private final boolean success;
	private final T value;
	private final String error;

	private Result(boolean success, T value, String error) {
		this.success = success;
		this.value = value;
		this.error = error;
	}

	public static <T> Result<T> success(T value) {
		return new Result<>(true, value, null);
	}

	public static <T> Result<T> failure(String error) {
		return new Result<>(false, null, error == null ? "Unknown error" : error);
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isFailure() {
		return !success;
	}

	public T getValue() {
		if (!success) {
			throw new IllegalStateException("Cannot get value of a failed result. Error: " + error);
		}
		return value;
	}

	public String getError() {
		if (success) {
			throw new IllegalStateException("Cannot get error of a successful result");
		}
		return error;
	}
}
