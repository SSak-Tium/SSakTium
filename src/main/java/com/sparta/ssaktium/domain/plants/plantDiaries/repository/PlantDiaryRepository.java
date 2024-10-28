package com.sparta.ssaktium.domain.plants.plantDiaries.repository;

import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlantDiaryRepository extends JpaRepository<PlantDiary, Long> {

    @Query("SELECT p FROM PlantDiary p WHERE p.plant.id = :plantId ORDER BY p.itemDate DESC")
    Page<PlantDiary> findAllByPlantId(@Param("plantId") Long plantId, Pageable pageable);


}
