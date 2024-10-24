package com.sparta.ssaktium.config;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<?>> handlerGlobalException(GlobalException e) {
        return new ResponseEntity<>(ApiResponse.error(e.getMessage()), e.getHttpStatus());
    }
}
