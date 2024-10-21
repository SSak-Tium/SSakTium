package com.sparta.springusersetting.domain.user.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USER;

public class NotFoundUserException extends GlobalException {
    public NotFoundUserException() {
        super(NOT_FOUND_USER);
    }
}

