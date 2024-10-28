package com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto;

import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import lombok.Getter;

@Getter
public class PlantDiaryResponseDto {

    private Long id;
    private Long plantId;
    private String title;
    private String content;
    private String imageUrl;
    private String itemDate;

    public PlantDiaryResponseDto(PlantDiary plantDiary) {
        this.id = plantDiary.getId();
        this.plantId = plantDiary.getPlant().getId();
        this.title = plantDiary.getTitle();
        this.content = plantDiary.getContent();
        this.imageUrl = plantDiary.getImageUrl();
        this.itemDate = plantDiary.getItemDate().toString();
    }
}
