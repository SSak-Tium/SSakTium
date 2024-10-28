package com.sparta.ssaktium.domain.plants.plantDiaries.entity;

import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class PlantDiary extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    private String content;

    private String imageUrl;

    private LocalDate itemDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PlantDiary(Plant plant, String content, String imageUrl, LocalDate itemDate, User user) {
        this.plant = plant;
        this.content = content;
        this.imageUrl = imageUrl;
        this.itemDate = itemDate;
        this.user = user;
    }

    public void update(String content, LocalDate itemDate, String imageUrl) {
        this.content = content;
        this.itemDate = itemDate;
        this.imageUrl = imageUrl;
    }
}
