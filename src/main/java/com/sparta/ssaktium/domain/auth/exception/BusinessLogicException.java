package com.sparta.ssaktium.domain.auth.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.UNABLE_TO_SEND_EMAIL;

public class BusinessLogicException extends GlobalException {
    public BusinessLogicException() {
        super(UNABLE_TO_SEND_EMAIL);
    }
}