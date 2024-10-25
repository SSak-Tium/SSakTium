package com.sparta.ssaktium.domain.boards.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_USER_OF_BOARD;


public class NotUserOfBoardException extends GlobalException {
    public  NotUserOfBoardException(){super (NOT_USER_OF_BOARD);}
}
