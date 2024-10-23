package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_USER_OF_LIKE;

public class LikeOwnerMismatchException extends GlobalException {
    public LikeOwnerMismatchException() {
        super(NOT_USER_OF_LIKE);
    }
}

