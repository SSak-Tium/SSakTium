package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.DUPLICATE_LIKE;

public class AlreadyLikedException extends GlobalException {
    public AlreadyLikedException() {
        super(DUPLICATE_LIKE);
    }
}

