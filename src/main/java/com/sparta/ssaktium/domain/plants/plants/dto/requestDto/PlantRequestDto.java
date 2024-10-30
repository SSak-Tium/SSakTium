package com.sparta.ssaktium.domain.plants.plants.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlantRequestDto {

    @Schema(description = "식물 이름", example = "연필 선인장")
    private String plantName;
    @Schema(description = "식물 애칭", example = "색연필 한다스")
    private String plantNickname;

    public PlantRequestDto(String plantName, String plantNickname) {
        this.plantName = plantName;
        this.plantNickname = plantNickname;
    }
}
