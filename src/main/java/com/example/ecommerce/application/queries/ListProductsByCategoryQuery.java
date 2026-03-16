package com.example.ecommerce.application.queries;

public class ListProductsByCategoryQuery {
    private final String category;

    public ListProductsByCategoryQuery(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
