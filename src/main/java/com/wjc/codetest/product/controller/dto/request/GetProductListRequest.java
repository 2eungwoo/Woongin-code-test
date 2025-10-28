package com.wjc.codetest.product.controller.dto.request;

public record GetProductListRequest(
        String category,
        int page,
        int size
) {
}