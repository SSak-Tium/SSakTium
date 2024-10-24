package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.LIKE_COUNT_UNDERFLOW;


public class LikeCountUnderflowException extends GlobalException {
    public LikeCountUnderflowException() {
        super(LIKE_COUNT_UNDERFLOW);
    }
}

