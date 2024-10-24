package com.sparta.ssaktium.domain.dictionaries.entitiy;

import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "dictionaries")
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String userName;
    private String imageUrl;

    public Dictionary(String title, String content, String userName, String imageUrl) {
        this.title = title;
        this.content = content;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public Dictionary(DictionaryRequestDto dictionaryRequestDto, String userName, String imageUrl) {
        this.title = dictionaryRequestDto.getTitle();
        this.content = dictionaryRequestDto.getContent();
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public void update(DictionaryUpdateRequestDto dictionaryUpdateRequestDto, String imageUrl) {
        this.title = dictionaryUpdateRequestDto.getTitle();
        this.content = dictionaryUpdateRequestDto.getContent();
        this.imageUrl = imageUrl;
    }
}
