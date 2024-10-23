package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class FriendRequestAlreadySentException extends GlobalException {
    public FriendRequestAlreadySentException() {
        super(GlobalExceptionConst.ALREADY_REQUEST);
    }
}
