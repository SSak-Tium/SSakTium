package com.sparta.springusersetting.domain.likes.commentLikes.entity;

import com.sparta.springusersetting.domain.comments.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommentLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable =false)
    private Comment comment;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    public CommentLikes(Comment comment, Long userId){
        this.comment = comment;
        this.userId = userId;
    }
}
