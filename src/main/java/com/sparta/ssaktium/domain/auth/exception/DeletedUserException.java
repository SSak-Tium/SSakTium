package com.sparta.ssaktium.domain.auth.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.DELETED_USER;

public class DeletedUserException extends GlobalException {
    public DeletedUserException() {
        super(DELETED_USER);
    }
}