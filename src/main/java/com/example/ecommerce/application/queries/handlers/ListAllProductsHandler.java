package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.dto.ProductResponse;
import com.example.ecommerce.application.queries.ListAllProductsQuery;
import com.example.ecommerce.domain.repositories.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListAllProductsHandler {
    private final ProductRepository productRepository;

    public ListAllProductsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Result<List<ProductResponse>> handle(ListAllProductsQuery query) {
        List<ProductResponse> products = productRepository.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getCategory(),
                        p.getPrice().amount(), p.getStock()))
                .collect(Collectors.toList());
        return Result.success(products);
    }
}
