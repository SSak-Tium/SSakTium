package com.sparta.ssaktium.domain.plants.plants.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlantUpdateRequestDto {

    @Schema(description = "식물 이름", example = "다육이")
    private String plantName;

    @Schema(description = "식물 애칭", example = "다칠이")
    private String plantNickname;

    @Schema(description = "식물 수정이미지")
    private String imageUrl;

}
