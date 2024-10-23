package com.sparta.ssaktium.domain.auth.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.INVALID_PASSWORD;

public class InvalidPasswordFormatException extends GlobalException {
    public InvalidPasswordFormatException() {
        super(INVALID_PASSWORD);
    }
}
