package com.sparta.ssaktium.domain.auth.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.ERROR_EMAIL_NOT_VERIFIED;

public class EmailNotVerifiedException extends GlobalException {
    public EmailNotVerifiedException() {
        super(ERROR_EMAIL_NOT_VERIFIED);
    }
}
