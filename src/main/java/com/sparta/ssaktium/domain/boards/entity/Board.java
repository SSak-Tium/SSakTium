package com.sparta.ssaktium.domain.boards.entity;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.likes.exception.LikeCountUnderflowException;
import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private int boardLikesCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    public Board(BoardSaveRequestDto boardSaveRequestDto, User user, String imageUrl) {
        this.title = boardSaveRequestDto.getTitle();
        this.content = boardSaveRequestDto.getContents();
        this.publicStatus = boardSaveRequestDto.getPublicStatus();
        this.user = user;
        this.imageUrl = imageUrl;
        this.statusEnum = StatusEnum.ACTIVATED;
    }

    public void updateBoards(BoardSaveRequestDto boardSaveRequestDto, String imageUrl) {
        this.title = boardSaveRequestDto.getTitle();
        this.content = boardSaveRequestDto.getContents();
        this.imageUrl = imageUrl;
        this.publicStatus = boardSaveRequestDto.getPublicStatus();
        this.statusEnum = StatusEnum.ACTIVATED;
    }

    // 좋아요 등록
    public void incrementLikesCount() {
        boardLikesCount++;
    }

    // 좋아요 취소
    public void decrementLikesCount() {
        if (boardLikesCount <= 0) {
            throw new LikeCountUnderflowException();
        }
        boardLikesCount--;
    }

    public void deleteBoards() {
        this.statusEnum = StatusEnum.DELETED;
    }
}
