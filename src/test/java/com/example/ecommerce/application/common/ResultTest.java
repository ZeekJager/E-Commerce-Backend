package com.example.ecommerce.application.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void success_isSuccessTrue() {
        Result<String> result = Result.success("ok");
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
    }

    @Test
    void success_getValue_returnsValue() {
        Result<String> result = Result.success("ok");
        assertEquals("ok", result.getValue());
    }

    @Test
    void success_getError_throws() {
        Result<String> result = Result.success("ok");
        assertThrows(IllegalStateException.class, result::getError);
    }

    @Test
    void failure_isFailureTrue() {
        Result<String> result = Result.failure("something went wrong");
        assertTrue(result.isFailure());
        assertFalse(result.isSuccess());
    }

    @Test
    void failure_getError_returnsError() {
        Result<String> result = Result.failure("something went wrong");
        assertEquals("something went wrong", result.getError());
    }

    @Test
    void failure_getValue_throws() {
        Result<String> result = Result.failure("oops");
        assertThrows(IllegalStateException.class, result::getValue);
    }

    @Test
    void failure_nullError_defaultsToUnknownError() {
        Result<String> result = Result.failure(null);
        assertEquals("Unknown error", result.getError());
    }
}
