package com.sparta.ssaktium.domain.orders.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_ORDER;

public class NotFoundOrderException extends GlobalException {
    public NotFoundOrderException() {
        super(NOT_FOUND_ORDER);
    }
}
