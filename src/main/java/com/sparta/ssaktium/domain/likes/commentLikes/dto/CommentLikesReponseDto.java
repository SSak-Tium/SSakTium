package com.sparta.ssaktium.domain.likes.commentLikes.dto;

import lombok.Getter;

@Getter
public class CommentLikesReponseDto {
    private final Long CommentId;
    private final int commentLikesCount;

    public CommentLikesReponseDto(Long commentId, int commentLikesCount) {
        CommentId = commentId;
        this.commentLikesCount = commentLikesCount;
    }
}
