package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ErrorResponse;
import com.example.ecommerce.api.dto.ProductRequest;
import com.example.ecommerce.application.commands.UpdateInventoryCommand;
import com.example.ecommerce.application.commands.handlers.UpdateInventoryHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.application.queries.handlers.GetProductByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListProductsByCategoryHandler;
import com.example.ecommerce.domain.entities.Product;
import com.example.ecommerce.domain.factories.ProductFactory;
import com.example.ecommerce.domain.valueobjects.Money;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final GetProductByIdHandler getProductByIdHandler;
    private final ListProductsByCategoryHandler listProductsByCategoryHandler;
    private final UpdateInventoryHandler updateInventoryHandler;
    private final ProductRepository productRepository;

    public ProductController(GetProductByIdHandler getProductByIdHandler,
                             ListProductsByCategoryHandler listProductsByCategoryHandler,
                             UpdateInventoryHandler updateInventoryHandler,
                             ProductRepository productRepository) {
        this.getProductByIdHandler = getProductByIdHandler;
        this.listProductsByCategoryHandler = listProductsByCategoryHandler;
        this.updateInventoryHandler = updateInventoryHandler;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request) {
        Product product = ProductFactory.create(
                request.getName(),
                request.getCategory(),
                new Money(request.getPrice(), "USD"),
                request.getStock()
        );
        productRepository.save(product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(location).body(toResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        List<ProductResponse> products = productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        Result<ProductResponse> existing = getProductByIdHandler.handle(new GetProductByIdQuery(id));
        if (!existing.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Product not found: " + id));
        }
        Product updated = new Product(id, request.getName(), request.getCategory(),
                new Money(request.getPrice(), "USD"), request.getStock());
        productRepository.save(updated);
        return ResponseEntity.ok(toResponse(updated));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable UUID id, @RequestParam int quantity) {
        Result<Void> result = updateInventoryHandler.handle(new UpdateInventoryCommand(id, quantity));
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        String error = result.getError();
        HttpStatus status = error.contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), error));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        Result<ProductResponse> existing = getProductByIdHandler.handle(new GetProductByIdQuery(id));
        if (!existing.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> listProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Result<List<ProductResponse>> result = listProductsByCategoryHandler.handle(
                new ListProductsByCategoryQuery(category));
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
        }
        List<ProductResponse> all = result.getValue();
        int fromIndex = page * size;
        if (fromIndex >= all.size()) {
            return ResponseEntity.ok(List.of());
        }
        int toIndex = Math.min(fromIndex + size, all.size());
        return ResponseEntity.ok(all.subList(fromIndex, toIndex));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice().amount(),
                product.getStock()
        );
    }
}
