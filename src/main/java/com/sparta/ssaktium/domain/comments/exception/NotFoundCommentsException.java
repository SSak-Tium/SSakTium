package com.sparta.ssaktium.domain.comments.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_COMMENT;


public class NotFoundCommentsException extends GlobalException {
    public NotFoundCommentsException() {
        super(NOT_FOUND_COMMENT);
    }
}

