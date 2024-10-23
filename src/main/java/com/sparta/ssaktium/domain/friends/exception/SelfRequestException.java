package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.DO_NOT_REQUEST_YOURSELF;

public class SelfRequestException extends GlobalException {
    public SelfRequestException() {
        super(DO_NOT_REQUEST_YOURSELF);
    }
}
