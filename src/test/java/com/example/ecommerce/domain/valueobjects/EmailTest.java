package com.example.ecommerce.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void constructor_acceptsValidEmail() {
        Email email = new Email("user@example.com");
        assertEquals("user@example.com", email.value());
    }

    @Test
    void constructor_rejectsNullValue() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @Test
    void constructor_rejectsMissingDomain() {
        assertThrows(IllegalArgumentException.class, () -> new Email("user@"));
    }

    @Test
    void constructor_rejectsMissingTld() {
        assertThrows(IllegalArgumentException.class, () -> new Email("user@example"));
    }

    @Test
    void constructor_rejectsNoAtSign() {
        assertThrows(IllegalArgumentException.class, () -> new Email("userexample.com"));
    }

    @Test
    void constructor_rejectsDotOnlyDomain() {
        assertThrows(IllegalArgumentException.class, () -> new Email("user@.com"));
    }
}
