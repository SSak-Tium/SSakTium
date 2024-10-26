package com.sparta.ssaktium.domain.plants.plants.repository;

import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findByUser(User user);

}
