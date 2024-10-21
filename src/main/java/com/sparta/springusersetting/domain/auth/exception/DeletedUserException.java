package com.sparta.springusersetting.domain.auth.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.DELETED_USER;

public class DeletedUserException extends GlobalException {
    public DeletedUserException() {
        super(DELETED_USER);
    }
}