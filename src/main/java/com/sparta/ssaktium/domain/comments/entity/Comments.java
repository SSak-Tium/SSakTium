package com.sparta.ssaktium.domain.comments.entity;

import com.sparta.ssaktium.domain.boards.entity.Boards;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.likes.exception.LikeCountUnderflowException;
import com.sparta.ssaktium.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Comments extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Boards board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public Comments(String content, Boards board, Users user){
        this.content = content;
        this.board = board;
        this.user = user;
    }

    private int commentLikesCount =0;

    public void incrementLikesCount(){
        commentLikesCount++;
    }

    public void decrementLikesCount(){
        if (commentLikesCount <= 0){
            throw new LikeCountUnderflowException();
        }
        commentLikesCount--;
    }

    // 댓글 수정 (작성자만 가능)
    public void updateComment(String content){
        this.content = content;
    }



}
