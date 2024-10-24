package com.sparta.ssaktium.domain.boards.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_BOARD;

public class NotFoundBoardsException extends GlobalException {
    public NotFoundBoardsException(){super (NOT_FOUND_BOARD);}
}
