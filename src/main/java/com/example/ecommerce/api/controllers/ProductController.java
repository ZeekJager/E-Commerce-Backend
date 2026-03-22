package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ProductRequest;
import com.example.ecommerce.api.dto.ProductResponse;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.application.queries.handlers.GetProductByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListProductsByCategoryHandler;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.factories.ProductFactory;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
        Product product = ProductFactory.create(
                request.getName(),
                request.getCategory(),
                new Money(BigDecimal.valueOf(request.getPrice()), "USD"),
                request.getStock()
        );

        productRepository.save(product);

        return Result.success(new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice().amount(),
                product.getStock()
        ));
    }

    @GetMapping("/{id}")
    public Result<ProductResponse> getProductById(@PathVariable UUID id) {
        return getProductByIdHandler.handle(new GetProductByIdQuery(id));
    }

    @GetMapping("/category/{category}")
    public Result<List<ProductResponse>> listProductsByCategory(@PathVariable String category) {
        return listProductsByCategoryHandler.handle(new ListProductsByCategoryQuery(category));
    }
}
