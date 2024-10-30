package com.sparta.ssaktium.domain.plants.plantDiaries.repository;

import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlantDiaryRepository extends JpaRepository<PlantDiary, Long> {

    @Query("SELECT p FROM PlantDiary p WHERE p.plant.id = :plantId AND p.plant.user.id = :userId ORDER BY p.itemDate DESC")
    Page<PlantDiary> findAllByPlantIdAndUserId(@Param("plantId") Long plantId, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT pd FROM PlantDiary pd WHERE pd.id = :plantDiaryId AND pd.user.id = :userId")
    Optional<PlantDiary> findByIdAndUserId(@Param("plantDiaryId") Long plantDiaryId, @Param("userId") Long userId);


}
