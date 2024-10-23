package com.sparta.ssaktium.domain.user.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USER;

public class NotFoundUserException extends GlobalException {
    public NotFoundUserException() {
        super(NOT_FOUND_USER);
    }
}

