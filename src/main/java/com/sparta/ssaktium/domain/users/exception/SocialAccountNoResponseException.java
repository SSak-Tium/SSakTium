package com.sparta.ssaktium.domain.users.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.SOCIAL_LINK_FAILED;

public class SocialAccountNoResponseException extends GlobalException {
    public SocialAccountNoResponseException() {
        super(SOCIAL_LINK_FAILED);
    }
}

