package com.sparta.ssaktium.domain.plants.plants.repository;

import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findAllByUser(User user);

    @Query("SELECT p FROM Plant p WHERE p.id = :plantId AND p.user.id = :userId")
    Optional<Plant> findByPlantIdAndUserId(@Param("plantId") Long plantId, @Param("userId") Long userId);


}
