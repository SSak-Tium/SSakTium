package com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto;

import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import lombok.Getter;

@Getter
public class PlantDiaryResponseDto {

    private Long id;
    private Long plantId;
    private String content;
    private String imageUrl;
    private String itemDate;
    private String created_at;
    private String modified_at;

    public PlantDiaryResponseDto(PlantDiary plantDiary) {
        this.id = plantDiary.getId();
        this.plantId = plantDiary.getPlant().getId();
        this.content = plantDiary.getContent();
        this.imageUrl = plantDiary.getImageUrl();
        this.itemDate = plantDiary.getItemDate().toString();
        this.created_at = plantDiary.getCreatedAt().toString();
        this.modified_at = plantDiary.getModifiedAt().toString();
    }

}
