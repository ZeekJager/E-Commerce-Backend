package com.example.ecommerce.application.queries.handlers;

import com.example.ecommerce.api.dto.ProductResponse;
import com.example.ecommerce.application.common.Result;
import com.example.ecommerce.application.queries.ListProductsByCategoryQuery;
import com.example.ecommerce.infrastructure.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListProductsByCategoryHandler {
	private final ProductRepository productRepository;

	public ListProductsByCategoryHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Result<List<ProductResponse>> handle(ListProductsByCategoryQuery query) {
		List<ProductResponse> products = productRepository.findByCategory(query.getCategory())
				.stream()
				.map(product -> new ProductResponse(
						product.getId(),
						product.getName(),
						product.getCategory(),
						product.getPrice().amount(),
						product.getStock()
				))
				.collect(Collectors.toList());

		if (products.isEmpty()) {
			return Result.failure("No products found in category: " + query.getCategory());
		}
		return Result.success(products);
	}
}
