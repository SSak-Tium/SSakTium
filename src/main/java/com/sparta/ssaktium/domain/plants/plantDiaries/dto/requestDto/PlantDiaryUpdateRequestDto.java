package com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlantDiaryUpdateRequestDto {

    private String title;
    private String content;
    private LocalDate itemDate;
    private String imageUrl;

}
