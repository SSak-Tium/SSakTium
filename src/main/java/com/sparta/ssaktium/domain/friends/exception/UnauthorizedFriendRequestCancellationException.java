package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class UnauthorizedFriendRequestCancellationException extends GlobalException {
    public UnauthorizedFriendRequestCancellationException() {
        super(GlobalExceptionConst.ERR_FRIEND_REQUEST_UNAUTHORIZED_CANCELLATION);
    }
}
