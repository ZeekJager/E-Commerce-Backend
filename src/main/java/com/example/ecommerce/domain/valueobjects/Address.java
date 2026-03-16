package com.example.ecommerce.domain.valueobjects;

import java.util.Objects;

public class Address {
    private final String street;
    private final String city;
    private final String zipCode;
    private final String country;

    public Address(String street, String city, String zipCode, String country) {
        if (street == null || city == null || zipCode == null || country == null) {
            throw new IllegalArgumentException("Address fields cannot be null");
        }
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String street() { return street; }
    public String city() { return city; }
    public String zipCode() { return zipCode; }
    public String country() { return country; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return street.equals(address.street) &&
                city.equals(address.city) &&
                zipCode.equals(address.zipCode) &&
                country.equals(address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, zipCode, country);
    }
}