package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        Product product = ProductFactory.create(
                request.getName(),
                request.getCategory(),
                new Money(BigDecimal.valueOf(request.getPrice()), "USD"),
                request.getStock()
        );
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice().amount(),
                product.getStock()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Result<ProductResponse> result = getProductByIdHandler.handle(new GetProductByIdQuery(id));
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> listProductsByCategory(@PathVariable String category) {
        Result<List<ProductResponse>> result = listProductsByCategoryHandler.handle(new ListProductsByCategoryQuery(category));
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
    }
}
