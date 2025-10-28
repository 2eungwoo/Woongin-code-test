package com.wjc.codetest.product.service.validator;

import com.wjc.codetest.product.global.shared.CustomException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomException {
    public ProductNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "해당 프로덕트를 찾을 수 없습니다 ID :: " + id);
    }
}