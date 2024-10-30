package com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlantDiaryUpdateRequestDto {

    @Schema(name = "제목", example = "제목")
    private String title;

    @Schema(name = "내용", example = "내용")
    private String content;

    @Schema(name = "활동날짜", example = "2022-03-14")
    private LocalDate itemDate;

    @Schema(name = "수정할 이미지 주소")
    private String imageUrl;

    public PlantDiaryUpdateRequestDto(String title, String content, LocalDate itemDate, String imageUrl) {
        this.title = title;
        this.content = content;
        this.itemDate = itemDate;
        this.imageUrl = imageUrl;
    }
}
