package com.sparta.springusersetting.domain.dictionaries.dto.response;

import com.sparta.springusersetting.domain.dictionaries.entitiy.Dictionaries;
import lombok.Getter;

@Getter
public class DictionaryResponseDto {

    private String title;
    private String content;
    private String userName;

    public DictionaryResponseDto(String title, String content, String userName) {
        this.title = title;
        this.content = content;
        this.userName = userName;
    }
}
