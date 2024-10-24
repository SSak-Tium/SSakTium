package com.sparta.ssaktium.domain.likes.boardLikes.entity;

import com.sparta.ssaktium.domain.boards.entity.Boards;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BoardLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable =false)
    private Boards board;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    public BoardLikes(Boards board, Long userId){
        this.board = board;
        this.userId = userId;
    }

}
