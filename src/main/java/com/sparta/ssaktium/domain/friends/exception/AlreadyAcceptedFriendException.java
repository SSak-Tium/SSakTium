package com.sparta.ssaktium.domain.friends.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class AlreadyAcceptedFriendException extends GlobalException {
    public AlreadyAcceptedFriendException() {
        super(GlobalExceptionConst.ERR_ALREADY_ACCEPTED_FRIEND);
    }
}
