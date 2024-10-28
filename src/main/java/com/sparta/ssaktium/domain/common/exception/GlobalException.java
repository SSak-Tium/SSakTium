package com.sparta.ssaktium.domain.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class GlobalException extends RuntimeException {
    private final HttpStatus httpStatus;

    public GlobalException(GlobalExceptionConst globalExceptionConst) {
        super(globalExceptionConst.getHttpStatus() + " " + globalExceptionConst.name() + " " + globalExceptionConst.getMessage());
        log.info(globalExceptionConst.getHttpStatus() + " " + globalExceptionConst.name() + " " + globalExceptionConst.getMessage());
        this.httpStatus = globalExceptionConst.getHttpStatus();
    }
}