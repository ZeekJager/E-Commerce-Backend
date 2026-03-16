package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ProductRequest;
import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.application.queries.handlers.GetProductByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListProductsByCategoryHandler;
import com.example.ecommerce.api.dto.ProductResponse;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.domain.valueobjects.Quantity;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final GetProductByIdHandler getProductByIdHandler;
    private final ListProductsByCategoryHandler listProductsByCategoryHandler;
    private final ProductRepository productRepository;

    public ProductController(GetProductByIdHandler getProductByIdHandler,
                             ListProductsByCategoryHandler listProductsByCategoryHandler,
                             ProductRepository productRepository) {
        this.getProductByIdHandler = getProductByIdHandler;
        this.listProductsByCategoryHandler = listProductsByCategoryHandler;
        this.productRepository = productRepository;
    }
    @PostMapping
    public Result<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        Product product = new Product(
                UUID.randomUUID().toString(),
                request.getName(),
                request.getCategory(),
                new Money(request.getPrice(), "USD"),
                new Quantity(request.getStock())
        );

        productRepository.save(product);

        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice().amount(),
                product.getStock().value()
        );

        return Result.success(response);
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