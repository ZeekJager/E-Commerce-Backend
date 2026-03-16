package com.example.ecommerce.domain.valueobjects;

import java.util.Objects;

public class Quantity {
    private final int value;

    public Quantity(int value) {
        if (value < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.value = value;
    }

    public int value() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}