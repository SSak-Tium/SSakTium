package com.sparta.springusersetting.domain.comments.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_COMMENT;


public class NotFoundCommentException extends GlobalException {
    public NotFoundCommentException() {
        super(NOT_FOUND_COMMENT);
    }
}

