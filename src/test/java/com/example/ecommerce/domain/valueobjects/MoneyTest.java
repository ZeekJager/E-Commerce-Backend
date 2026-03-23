package com.example.ecommerce.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void constructor_rejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(BigDecimal.valueOf(-1), "USD"));
    }

    @Test
    void constructor_rejectsNullCurrency() {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(BigDecimal.ONE, null));
    }

    @Test
    void add_sumsTwoAmounts() {
        Money a = new Money(BigDecimal.valueOf(10.50), "USD");
        Money b = new Money(BigDecimal.valueOf(4.50), "USD");
        assertEquals(BigDecimal.valueOf(15.00), a.add(b).amount());
    }

    @Test
    void add_rejectsMismatchedCurrencies() {
        Money usd = new Money(BigDecimal.TEN, "USD");
        Money eur = new Money(BigDecimal.TEN, "EUR");
        assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
    }

    @Test
    void multiply_scalesAmount() {
        Money price = new Money(BigDecimal.valueOf(25), "USD");
        assertEquals(BigDecimal.valueOf(75), price.multiply(3).amount());
    }

    @Test
    void equals_trueForSameAmountAndCurrency() {
        Money a = new Money(BigDecimal.valueOf(10), "USD");
        Money b = new Money(BigDecimal.valueOf(10), "USD");
        assertEquals(a, b);
    }
}
