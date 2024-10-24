package com.sparta.ssaktium.domain.plants.repository;

import com.sparta.ssaktium.domain.plants.entity.Plant;
import com.sparta.ssaktium.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findByUserId(Users users);

}
