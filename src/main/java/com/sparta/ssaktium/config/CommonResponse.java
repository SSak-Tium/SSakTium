package com.sparta.ssaktium.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public class CommonResponse<T> {

    private enum Status {
        SUCCESS, FAIL
    }

    private final Status status;
    private final T data;
    private final String message;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(Status.SUCCESS, data, "요청이 성공적으로 처리되었습니다");
    }


    public static CommonResponse<?> error(String message) {
        return new CommonResponse<>(Status.FAIL, "", message);
    }
}
