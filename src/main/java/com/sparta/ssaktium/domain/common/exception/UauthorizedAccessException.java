package com.sparta.ssaktium.domain.common.exception;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.UNAUTHORIZED_ACCESS;

public class UauthorizedAccessException extends GlobalException{
    public UauthorizedAccessException() {
        super(UNAUTHORIZED_ACCESS);
    }
}
