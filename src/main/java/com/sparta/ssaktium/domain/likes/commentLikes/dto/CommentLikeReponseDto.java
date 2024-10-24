package com.sparta.ssaktium.domain.likes.commentLikes.dto;

import lombok.Getter;

@Getter
public class CommentLikeReponseDto {
    private final Long CommentId;
    private final int commentLikesCount;

    public CommentLikeReponseDto(Long commentId, int commentLikesCount) {
        CommentId = commentId;
        this.commentLikesCount = commentLikesCount;
    }
}
