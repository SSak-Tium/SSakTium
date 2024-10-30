package com.sparta.ssaktium.domain.common.exception;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.FORBIDDEN_ERROR;

public class ForbiddenException extends GlobalException {
    public ForbiddenException() {
        super(FORBIDDEN_ERROR);
    }
}