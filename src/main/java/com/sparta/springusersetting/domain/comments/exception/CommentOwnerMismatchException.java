package com.sparta.springusersetting.domain.comments.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_USER_OF_COMMENT;

public class CommentOwnerMismatchException extends GlobalException {
    public CommentOwnerMismatchException() {
        super(NOT_USER_OF_COMMENT);
    }
}
