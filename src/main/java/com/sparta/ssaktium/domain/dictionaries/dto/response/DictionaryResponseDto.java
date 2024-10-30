package com.sparta.ssaktium.domain.dictionaries.dto.response;

import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import lombok.Getter;

@Getter
public class DictionaryResponseDto {

    private long id;
    private String title;
    private String content;
    private String imageUrl;

    public DictionaryResponseDto(Dictionary dictionary) {
        this.id = dictionary.getId();
        this.title = dictionary.getTitle();
        this.content = dictionary.getContent();
        this.imageUrl = dictionary.getImageUrl();
    }
}
