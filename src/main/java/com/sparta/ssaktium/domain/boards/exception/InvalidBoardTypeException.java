package com.sparta.ssaktium.domain.boards.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class InvalidBoardTypeException extends GlobalException {
    public InvalidBoardTypeException() {
        super(GlobalExceptionConst.INVALID_BOARD_TYPE);
    }
}
