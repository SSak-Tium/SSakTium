package com.sparta.ssaktium.domain.boards.entity;

import com.sparta.ssaktium.domain.likes.exception.LikeCountUnderflowException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int boardLikesCount =0;

    public void incrementLikesCount(){
        boardLikesCount++;
    }

    public void decrementLikesCount(){
        if (boardLikesCount <= 0){
            throw new LikeCountUnderflowException();
        }
        boardLikesCount--;
    }
}
