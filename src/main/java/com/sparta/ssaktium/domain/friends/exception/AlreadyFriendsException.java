package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class AlreadyFriendsException extends GlobalException {
    public AlreadyFriendsException() {
        super(GlobalExceptionConst.ERR_ALREADY_FRIEND);
    }
}
