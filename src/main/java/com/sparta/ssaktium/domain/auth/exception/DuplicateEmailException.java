package com.sparta.ssaktium.domain.auth.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.DUPLICATE_EMAIL;

public class DuplicateEmailException extends GlobalException {
    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL);
    }
}
