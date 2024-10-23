package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class NotFoundRequestFriendException extends GlobalException {
    public NotFoundRequestFriendException() {
        super(GlobalExceptionConst.NOT_FOUND_FREIND_REQUEST);
    }
}
