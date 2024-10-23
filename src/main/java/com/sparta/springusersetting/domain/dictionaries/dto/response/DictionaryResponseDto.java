package com.sparta.springusersetting.domain.dictionaries.dto.response;

import lombok.Getter;

@Getter
public class DictionaryResponseDto {

    private String title;
    private String content;
    private String userName;
    private String imageUrl;

    public DictionaryResponseDto(String title, String content, String userName, String imageUrl) {
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }
}
