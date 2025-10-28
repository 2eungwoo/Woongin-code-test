package com.wjc.codetest.product.controller.dto.response;

import com.wjc.codetest.product.model.domain.Product;

public record ProductResponse(
    Long id,
    String category,
    String name
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getCategory(),
            product.getName()
        );
    }
}