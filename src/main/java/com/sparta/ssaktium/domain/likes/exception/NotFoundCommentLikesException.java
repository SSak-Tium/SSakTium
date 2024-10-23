package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_COMMENT_LIKE;


public class NotFoundCommentLikesException extends GlobalException {
    public NotFoundCommentLikesException() {
        super(NOT_FOUND_COMMENT_LIKE);
    }
}

