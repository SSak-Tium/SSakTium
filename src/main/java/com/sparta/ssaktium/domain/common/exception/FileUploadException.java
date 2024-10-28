package com.sparta.ssaktium.domain.common.exception;

import static com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst.FILE_UPLOAD_ERROR;

public class FileUploadException extends GlobalException {
    public FileUploadException() {
        super(FILE_UPLOAD_ERROR);
    }
}