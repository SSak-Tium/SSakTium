package com.sparta.ssaktium.domain.plants.dto.responseDto;

import lombok.Getter;

@Getter
public class PlantResponseDto {

    private Long id;
    private Long userId;
    private String plantName;
    private String plantNickname;
    private String imageUrl;

    public PlantResponseDto(Long id, Long userId, String plantName, String plantNickname, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.plantName = plantName;
        this.plantNickname = plantNickname;
        this.imageUrl = imageUrl;
    }
}
