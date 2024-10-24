package com.sparta.ssaktium.domain.likes.commentLikes.entity;

import com.sparta.ssaktium.domain.comments.entity.Comments;
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
    private Comments comments;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    public CommentLikes(Comments comments, Long userId){
        this.comments = comments;
        this.userId = userId;
    }
}
