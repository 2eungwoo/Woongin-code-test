package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductListResponse(
        List<Product> products,
        int totalPages,
        long totalElements,
        int page
) {
    public static ProductListResponse from(Page<Product> page) {
        return new ProductListResponse(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber()
        );
    }
}
