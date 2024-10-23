package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_BOARD_LIKE;

public class NotFoundBoardLikesException extends GlobalException {
    public NotFoundBoardLikesException() {
        super(NOT_FOUND_BOARD_LIKE);
    }
}

