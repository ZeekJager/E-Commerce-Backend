package com.example.ecommerce.api.controllers;

import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.application.queries.handlers.GetProductByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListProductsByCategoryHandler;
import com.example.ecommerce.api.dto.ProductResponse;
import com.example.ecommerce.application.common.Result;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final GetProductByIdHandler getProductByIdHandler;
    private final ListProductsByCategoryHandler listProductsByCategoryHandler;

    public ProductController(GetProductByIdHandler getProductByIdHandler,
                             ListProductsByCategoryHandler listProductsByCategoryHandler) {
        this.getProductByIdHandler = getProductByIdHandler;
        this.listProductsByCategoryHandler = listProductsByCategoryHandler;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Result<List<ProductResponse>>> listProductsByCategory(
            @PathVariable @NotBlank(message = "Category must not be blank") String category) {
        logger.info("Listing products for category: {}", category);
        try {
            Result<List<ProductResponse>> result = listProductsByCategoryHandler.handle(new ListProductsByCategoryQuery(category));
            logger.debug("Listed products for category '{}', success: {}", category, result.isSuccess());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error listing products for category '{}': {}", category, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Result.failure("An unexpected error occurred"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<ProductResponse>> getProductById(
            @PathVariable @NotBlank(message = "Product ID must not be blank") String id) {
        logger.info("Fetching product with ID: {}", id);
        try {
            Result<ProductResponse> result = getProductByIdHandler.handle(new GetProductByIdQuery(id));
            if (!result.isSuccess()) {
                logger.warn("Product not found for ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.debug("Successfully retrieved product with ID: {}", id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error fetching product with ID '{}': {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Result.failure("An unexpected error occurred"));
        }
    }
}
