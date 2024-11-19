package com.sparta.ssaktium.domain.products.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_PRODUCT;

public class NotFountProductException extends GlobalException {
    public NotFountProductException() {
        super(NOT_FOUND_PRODUCT);
    }
}

