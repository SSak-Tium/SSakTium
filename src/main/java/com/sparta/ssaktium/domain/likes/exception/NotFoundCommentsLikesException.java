package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_COMMENT_LIKE;


public class NotFoundCommentsLikesException extends GlobalException {
    public NotFoundCommentsLikesException() {
        super(NOT_FOUND_COMMENT_LIKE);
    }
}

