package com.sparta.ssaktium.domain.plants.dto.responseDto;

import com.sparta.ssaktium.domain.plants.entity.Plant;
import lombok.Getter;

@Getter
public class PlantResponseDto {

    private Long id;
    private Long userId;
    private String plantName;
    private String plantNickname;
    private String imageUrl;

    public PlantResponseDto(Plant plant) {
        this.id = plant.getId();
        this.userId = plant.getUser().getId();
        this.plantName = plant.getPlantName();
        this.plantNickname = plant.getPlantNickname();
        this.imageUrl = plant.getImageUrl();
    }
}
