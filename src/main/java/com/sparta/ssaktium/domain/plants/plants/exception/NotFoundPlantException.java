package com.sparta.ssaktium.domain.plants.plants.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class NotFoundPlantException extends GlobalException {
    public NotFoundPlantException() {
        super(GlobalExceptionConst.NOT_FOUND_PLANT);
    }
}
