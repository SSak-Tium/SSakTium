package com.sparta.ssaktium.domain.dictionaries.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.NOT_FOUND_DICTIONARY;

public class NotFoundDictionaryException extends GlobalException {
    public NotFoundDictionaryException() {
        super(NOT_FOUND_DICTIONARY);
    }
}

