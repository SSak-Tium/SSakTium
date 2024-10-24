package com.sparta.ssaktium.domain.likes.boardLikes.entity;

import com.sparta.ssaktium.domain.boards.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable =false)
    private Board board;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    public BoardLike(Board board, Long userId){
        this.board = board;
        this.userId = userId;
    }

}
