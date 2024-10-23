package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class NotFoundFriendException extends GlobalException {
    public NotFoundFriendException() {
        super(GlobalExceptionConst.NOT_FOUND_FREIND);
    }
}
