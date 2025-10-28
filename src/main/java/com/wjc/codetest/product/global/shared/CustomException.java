package com.wjc.codetest.product.global.shared;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{

    private final String errorCode;

    protected CustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
