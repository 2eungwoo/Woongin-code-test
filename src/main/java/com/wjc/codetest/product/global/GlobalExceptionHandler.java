package com.wjc.codetest.product.global;

import com.wjc.codetest.product.global.shared.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> customException(CustomException e) {
        log.error("CustomException - status:: {}, message :: {}",
            e.getHttpStatus(),
            e.getMessage()
        );

        return ResponseEntity
            .status(e.getHttpStatus())
            .body(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("Unexpected Exception - status :: {}, errorType :: {}, errorCause :: {}",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "runtimeException",
            e.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal Server Error");
    }
}
