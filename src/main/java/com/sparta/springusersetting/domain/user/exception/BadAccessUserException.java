package com.sparta.springusersetting.domain.user.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.INVALID_ROLE;

public class BadAccessUserException extends GlobalException {
    public BadAccessUserException() {
        super(INVALID_ROLE);
    }
}