package com.wjc.codetest.product.model.request;

public record GetProductListRequest(
        String category,
        int page,
        int size
) {
}