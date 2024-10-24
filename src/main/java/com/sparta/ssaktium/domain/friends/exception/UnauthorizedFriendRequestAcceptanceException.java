package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class UnauthorizedFriendRequestAcceptanceException extends GlobalException {
    public UnauthorizedFriendRequestAcceptanceException() {
        super(GlobalExceptionConst.ERR_UNAUTHORIZED_FRIEND_REQUEST_ACCEPTANCE);
    }
}
