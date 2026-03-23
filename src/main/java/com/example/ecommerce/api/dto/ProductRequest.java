package com.example.ecommerce.api.dto;

<<<<<<< Updated upstream
import java.util.Objects;

=======
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
>>>>>>> Stashed changes
public class ProductRequest {
    private String name;
    private String category;
    private double price;
    private int stock;


    public ProductRequest(String name, String category, double price, int stock) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
}