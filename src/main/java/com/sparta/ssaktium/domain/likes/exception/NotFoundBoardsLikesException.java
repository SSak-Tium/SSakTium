package com.sparta.ssaktium.domain.likes.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_BOARD_LIKE;

public class NotFoundBoardsLikesException extends GlobalException {
    public NotFoundBoardsLikesException() {
        super(NOT_FOUND_BOARD_LIKE);
    }
}

