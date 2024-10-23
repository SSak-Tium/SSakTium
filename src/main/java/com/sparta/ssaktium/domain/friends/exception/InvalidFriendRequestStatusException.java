package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class InvalidFriendRequestStatusException extends GlobalException {
    public InvalidFriendRequestStatusException() {
        super(GlobalExceptionConst.ERR_FRIEND_REQUEST_ALREADY_HANDLED);
    }
}
