package com.wjc.codetest.product.service.dto.response;

import java.util.List;

public record ProductListResponse(
    List<ProductResponse> products,
    int totalPages,
    long totalElements,
    int page
) {

}
