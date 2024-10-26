package com.sparta.ssaktium.domain.plants.plantDiaries.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlantDiaryRequestDto {

    private String content;
    private LocalDate itemDate;

}
