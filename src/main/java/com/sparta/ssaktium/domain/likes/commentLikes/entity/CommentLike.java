package com.sparta.ssaktium.domain.likes.commentLikes.entity;

import com.sparta.ssaktium.domain.comments.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable =false)
    private Comment comment;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    public CommentLike(Comment comment, Long userId){
        this.comment = comment;
        this.userId = userId;
    }
}
