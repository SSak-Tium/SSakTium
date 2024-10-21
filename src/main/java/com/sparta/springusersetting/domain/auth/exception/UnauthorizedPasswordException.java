package com.sparta.springusersetting.domain.auth.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.UNAUTHORIZED_PASSWORD;

public class UnauthorizedPasswordException extends GlobalException {
    public UnauthorizedPasswordException() {
        super(UNAUTHORIZED_PASSWORD);
    }
}
