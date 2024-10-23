package com.sparta.ssaktium.domain.user.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.DUPLICATE_PASSWORD;

public class DuplicatePasswordException extends GlobalException {
    public DuplicatePasswordException() {
        super(DUPLICATE_PASSWORD);
    }
}
