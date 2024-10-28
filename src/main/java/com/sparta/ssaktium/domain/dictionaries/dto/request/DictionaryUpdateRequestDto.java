package com.sparta.ssaktium.domain.dictionaries.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DictionaryUpdateRequestDto {

    private String title;
    private String content;
}