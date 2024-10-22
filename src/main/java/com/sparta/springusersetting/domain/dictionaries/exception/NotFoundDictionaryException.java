package com.sparta.springusersetting.domain.dictionaries.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_DICTIONARY;

public class NotFoundDictionaryException extends GlobalException {
    public NotFoundDictionaryException() {
        super(NOT_FOUND_DICTIONARY);
    }
}

