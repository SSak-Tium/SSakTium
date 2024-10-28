package com.sparta.ssaktium.domain.plants.plantDiaries.exception;

import com.sparta.ssaktium.domain.common.exception.GlobalException;
import com.sparta.ssaktium.domain.common.exception.GlobalExceptionConst;

public class NotFoundPlantDiaryException extends GlobalException {
    public NotFoundPlantDiaryException() {
        super(GlobalExceptionConst.NOT_FOUND_PLANTDIARY);
    }
}
