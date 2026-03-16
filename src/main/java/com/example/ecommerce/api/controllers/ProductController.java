package com.example.ecommerce.api.controllers;

import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.application.queries.handlers.GetProductByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListProductsByCategoryHandler;
import com.example.ecommerce.api.dto.ProductResponse;
import com.example.ecommerce.application.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final GetProductByIdHandler getProductByIdHandler;
    private final ListProductsByCategoryHandler listProductsByCategoryHandler;

    public ProductController(GetProductByIdHandler getProductByIdHandler,
                             ListProductsByCategoryHandler listProductsByCategoryHandler) {
        this.getProductByIdHandler = getProductByIdHandler;
        this.listProductsByCategoryHandler = listProductsByCategoryHandler;
    }

    @GetMapping("/{id}")
    public Result<ProductResponse> getProductById(@PathVariable String id) {
        return getProductByIdHandler.handle(new GetProductByIdQuery(id));
    }

    @GetMapping("/category/{category}")
    public Result<List<ProductResponse>> listProductsByCategory(@PathVariable String category) {
        return listProductsByCategoryHandler.handle(new ListProductsByCategoryQuery(category));
    }
}