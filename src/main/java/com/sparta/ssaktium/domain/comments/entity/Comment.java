package com.sparta.ssaktium.domain.comments.entity;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.likes.exception.LikeCountUnderflowException;
import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "comments")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private int commentLikesCount = 0;

    public Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public void incrementLikesCount(){
        commentLikesCount++;
    }

    public void decrementLikesCount() {
        if (commentLikesCount <= 0) {
            throw new LikeCountUnderflowException();
        }
        commentLikesCount--;
    }

    // 댓글 수정 (작성자만 가능)
    public void updateComment(String content) {
        this.content = content;
    }


}
