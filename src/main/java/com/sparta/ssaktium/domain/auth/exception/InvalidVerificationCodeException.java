package com.sparta.ssaktium.domain.auth.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.ERROR_INVALID_VERIFICATION_CODE;

public class InvalidVerificationCodeException extends GlobalException {
    public InvalidVerificationCodeException() {
        super(ERROR_INVALID_VERIFICATION_CODE);
    }
}
