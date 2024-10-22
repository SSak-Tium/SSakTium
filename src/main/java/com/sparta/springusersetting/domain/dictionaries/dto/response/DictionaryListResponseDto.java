package com.sparta.springusersetting.domain.dictionaries.dto.response;

import lombok.Getter;

@Getter
public class DictionaryListResponseDto {
    private String title;
    private String userName;

    public DictionaryListResponseDto(String title, String userName) {
        this.title = title;
        this.userName = userName;
    }
}
