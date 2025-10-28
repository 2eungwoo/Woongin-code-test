package com.wjc.codetest.product.global;

import com.wjc.codetest.product.global.shared.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    @ResponseBody // 도메인별 예외 중 커스텀예외를 상속받은 예외클래스 경우
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e) {
        log.error("status:: {}, message :: {}",
            e.getHttpStatus(),
            e.getMessage()
        );

        return ResponseEntity
            .status(e.getHttpStatus())
            .body(e.getMessage());
    }

    @ResponseBody // '@Valid' '@RequestBody' 에서 검증 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
            .findFirst()
            .orElse("잘못된 요청 값입니다.");

        log.error("status :: {}, errorType :: {}, errorCause :: {}",
            HttpStatus.BAD_REQUEST,
            "methodArgumentNotValidException",
            message
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(message);
    }

    @ResponseBody // 요청 본문 json 불일치 예외
    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("[%s] %s", error.getField(), error.getDefaultMessage()))
            .findFirst()
            .orElse("요청 바인딩 오류입니다.");

        log.error("status :: {}, errorType :: {}, errorCause :: {}",
            HttpStatus.BAD_REQUEST,
            "bindException",
            message
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(message);
    }

    @ResponseBody // 요청 파라미터 잘못 됐을 때 예외
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
            HttpStatus.BAD_REQUEST,
            "constraintViolationException",
            e.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ResponseBody // 그 외 나머지 예외
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnexpectedException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "runtimeException",
            e.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Internal Server Error");
    }
}
