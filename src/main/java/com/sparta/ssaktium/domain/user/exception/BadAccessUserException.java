package com.sparta.ssaktium.domain.user.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.INVALID_ROLE;

public class BadAccessUserException extends GlobalException {
    public BadAccessUserException() {
        super(INVALID_ROLE);
    }
}