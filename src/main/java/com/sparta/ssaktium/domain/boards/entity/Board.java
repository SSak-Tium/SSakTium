package com.sparta.ssaktium.domain.boards.entity;

import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.likes.exception.LikeCountUnderflowException;
import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "boards")
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int boardLikesCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BoardImages> imageUrls;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Board(String title, String content, PublicStatus publicStatus, User user) {
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
        this.user = user;
        this.statusEnum = StatusEnum.ACTIVATED;
    }

    public void updateBoards(String title, String content, PublicStatus publicStatus) {
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
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
