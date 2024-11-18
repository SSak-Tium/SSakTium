package com.sparta.ssaktium.domain.comments.dto.response;

import com.sparta.ssaktium.domain.comments.entity.Comment;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Long boardId;
    private final Long userId;
    private final int commentLikesCount;

    public CommentResponseDto(Long id,
                              String content,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt,
                              Long boardId,
                              Long userId,
                              int commentLikesCount) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.boardId = boardId;
        this.userId = userId;
        this.commentLikesCount = commentLikesCount;
    }

    // 객체 받아서 Dto 만들기
    public CommentResponseDto(Comment comment, int commentLikesCount) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.boardId = comment.getBoard().getId();
        this.userId = comment.getUser().getId();
        this.commentLikesCount = commentLikesCount;
    }
}




