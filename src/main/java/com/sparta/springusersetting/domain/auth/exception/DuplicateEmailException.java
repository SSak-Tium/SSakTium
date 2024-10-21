package com.sparta.springusersetting.domain.auth.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.DUPLICATE_EMAIL;

public class DuplicateEmailException extends GlobalException {
    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL);
    }
}
