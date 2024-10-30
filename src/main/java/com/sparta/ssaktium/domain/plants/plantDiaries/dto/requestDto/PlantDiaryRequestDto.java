package com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlantDiaryRequestDto {

    @Schema(description = "제목", example = "제목")
    private String title;

    @Schema(description = "내용", example = "내용")
    private String content;

    @Schema(description = "활동날짜", example = "2021-12-14")
    private LocalDate itemDate;

}
