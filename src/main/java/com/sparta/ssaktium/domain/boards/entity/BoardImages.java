package com.sparta.ssaktium.domain.boards.entity;

import com.sparta.ssaktium.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BoardImages extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id", nullable = false)
    private Board board;

    public BoardImages(String imageUrl, Board board) {
        this.imageUrl = imageUrl;
        this.board = board;
    }

}
