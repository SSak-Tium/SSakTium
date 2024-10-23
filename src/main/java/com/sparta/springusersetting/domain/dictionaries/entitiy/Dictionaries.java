package com.sparta.springusersetting.domain.dictionaries.entitiy;

import com.sparta.springusersetting.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.springusersetting.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "dictionaries")
public class Dictionaries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String userName;
    private String imageUrl;

    public Dictionaries(DictionaryRequestDto dictionaryRequestDto, String userName, String imageUrl) {
        this.title = dictionaryRequestDto.getTitle();
        this.content = dictionaryRequestDto.getContent();
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public void update(DictionaryUpdateRequestDto dictionaryUpdateRequestDto) {
        this.title = dictionaryUpdateRequestDto.getTitle();
        this.content = dictionaryUpdateRequestDto.getContent();
    }
}
