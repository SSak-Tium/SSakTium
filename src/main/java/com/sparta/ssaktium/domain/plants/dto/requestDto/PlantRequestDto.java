package com.sparta.ssaktium.domain.plants.dto.requestDto;

import lombok.Getter;

@Getter
public class PlantRequestDto {

    private String plantName;
    private String plantNickname;

    public PlantRequestDto(String plantName, String plantNickname) {
        this.plantName = plantName;
        this.plantNickname = plantNickname;
    }
}
