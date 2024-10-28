package com.sparta.ssaktium.domain.plants.plantDiaries.repository;

import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantDiaryRepository extends JpaRepository<PlantDiary, Long> {
}
