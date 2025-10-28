package com.wjc.codetest.product.controller.dto.response;

import java.util.List;

public record ProductListResponse(
    List<ProductResponse> products,
    int totalPages,
    long totalElements,
    int page
) {

}
