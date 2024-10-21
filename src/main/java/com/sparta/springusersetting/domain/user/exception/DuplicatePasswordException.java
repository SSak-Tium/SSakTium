package com.sparta.springusersetting.domain.user.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.DUPLICATE_PASSWORD;

public class DuplicatePasswordException extends GlobalException {
    public DuplicatePasswordException() {
        super(DUPLICATE_PASSWORD);
    }
}
