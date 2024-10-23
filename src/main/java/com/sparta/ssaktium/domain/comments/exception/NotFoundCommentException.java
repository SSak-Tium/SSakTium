package com.sparta.ssaktium.domain.comments.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_COMMENT;


public class NotFoundCommentException extends GlobalException {
    public NotFoundCommentException() {
        super(NOT_FOUND_COMMENT);
    }
}

