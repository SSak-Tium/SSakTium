package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class NotFoundFriendRequestException extends GlobalException {
    public NotFoundFriendRequestException() {
        super(GlobalExceptionConst.NOT_FOUND_FREIND_REQUEST);
    }
}
