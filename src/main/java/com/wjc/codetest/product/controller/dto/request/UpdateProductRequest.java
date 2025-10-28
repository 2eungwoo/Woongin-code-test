package com.wjc.codetest.product.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateProductRequest(
    @NotBlank(message = "카테고리는 공백일 수 없습니다.")
    @Length(min = 1, max = 100, message = "카테고리는 100자 이내로 작성해주세요.") // 범위는 임의 설정
    String category,

    @NotBlank(message = "상품 이름은 공백일 수 없습니다.")
    @Length(min = 1, max = 100, message = "상품 이름은 100자 이내로 작성해주세요.") // 범위는 임의 설정
    String name
) {
}

