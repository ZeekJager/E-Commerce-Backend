package com.example.ecommerce.api.controllers;

import com.example.ecommerce.api.dto.ErrorResponse;
import com.example.ecommerce.api.dto.ProductRequest;
import com.example.ecommerce.application.commands.CreateProductCommand;
import com.example.ecommerce.application.commands.DeleteProductCommand;
import com.example.ecommerce.application.commands.UpdateInventoryCommand;
import com.example.ecommerce.application.commands.UpdateProductCommand;
import com.example.ecommerce.application.commands.handlers.CreateProductHandler;
import com.example.ecommerce.application.commands.handlers.DeleteProductHandler;
import com.example.ecommerce.application.commands.handlers.UpdateInventoryHandler;
import com.example.ecommerce.application.commands.handlers.UpdateProductHandler;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.application.queries.GetProductByIdQuery;
import com.example.ecommerce.application.queries.ListAllProductsQuery;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.application.queries.handlers.GetProductByIdHandler;
import com.example.ecommerce.application.queries.handlers.ListAllProductsHandler;
import com.example.ecommerce.application.queries.handlers.ListProductsByCategoryHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final CreateProductHandler createProductHandler;
    private final UpdateProductHandler updateProductHandler;
    private final DeleteProductHandler deleteProductHandler;
    private final UpdateInventoryHandler updateInventoryHandler;
    private final GetProductByIdHandler getProductByIdHandler;
    private final ListAllProductsHandler listAllProductsHandler;
    private final ListProductsByCategoryHandler listProductsByCategoryHandler;

    public ProductController(CreateProductHandler createProductHandler,
                             UpdateProductHandler updateProductHandler,
                             DeleteProductHandler deleteProductHandler,
                             UpdateInventoryHandler updateInventoryHandler,
                             GetProductByIdHandler getProductByIdHandler,
                             ListAllProductsHandler listAllProductsHandler,
                             ListProductsByCategoryHandler listProductsByCategoryHandler) {
        this.createProductHandler = createProductHandler;
        this.updateProductHandler = updateProductHandler;
        this.deleteProductHandler = deleteProductHandler;
        this.updateInventoryHandler = updateInventoryHandler;
        this.getProductByIdHandler = getProductByIdHandler;
        this.listAllProductsHandler = listAllProductsHandler;
        this.listProductsByCategoryHandler = listProductsByCategoryHandler;
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request) {
        Result<ProductResponse> result = createProductHandler.handle(
                new CreateProductCommand(request.getName(), request.getCategory(),
                        request.getPrice(), request.getStock()));
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), result.getError()));
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(result.getValue().getId()).toUri();
        return ResponseEntity.created(location).body(result.getValue());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listAllProducts() {
        return ResponseEntity.ok(listAllProductsHandler.handle(new ListAllProductsQuery()).getValue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Result<ProductResponse> result = getProductByIdHandler.handle(new GetProductByIdQuery(id));
        if (result.isSuccess()) return ResponseEntity.ok(result.getValue());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id,
                                           @Valid @RequestBody ProductRequest request) {
        Result<ProductResponse> result = updateProductHandler.handle(
                new UpdateProductCommand(id, request.getName(), request.getCategory(),
                        request.getPrice(), request.getStock()));
        if (!result.isSuccess()) {
            HttpStatus status = result.getError().contains("not found")
                    ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status)
                    .body(new ErrorResponse(status.value(), result.getError()));
        }
        return ResponseEntity.ok(result.getValue());
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable UUID id, @RequestParam int quantity) {
        Result<Void> result = updateInventoryHandler.handle(new UpdateInventoryCommand(id, quantity));
        if (result.isSuccess()) return ResponseEntity.noContent().build();
        HttpStatus status = result.getError().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), result.getError()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        Result<Void> result = deleteProductHandler.handle(new DeleteProductCommand(id));
        if (result.isSuccess()) return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), result.getError()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> listProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Result<List<ProductResponse>> result = listProductsByCategoryHandler.handle(
                new ListProductsByCategoryQuery(category));
        List<ProductResponse> all = result.getValue();
        int fromIndex = page * size;
        if (fromIndex >= all.size()) return ResponseEntity.ok(List.of());
        return ResponseEntity.ok(all.subList(fromIndex, Math.min(fromIndex + size, all.size())));
    }
}
