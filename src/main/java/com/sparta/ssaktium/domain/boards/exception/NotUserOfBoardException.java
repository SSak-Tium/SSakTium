package com.sparta.ssaktium.domain.boards.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;


public class NotUserOfBoardException extends GlobalException {
    public NotUserOfBoardException() {
        super(GlobalExceptionConst.NOT_USER_OF_BOARD);
    }
}
