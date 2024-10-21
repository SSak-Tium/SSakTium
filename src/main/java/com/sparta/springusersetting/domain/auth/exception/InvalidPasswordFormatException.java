package com.sparta.springusersetting.domain.auth.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.INVALID_PASSWORD;

public class InvalidPasswordFormatException extends GlobalException {
    public InvalidPasswordFormatException() {
        super(INVALID_PASSWORD);
    }
}
