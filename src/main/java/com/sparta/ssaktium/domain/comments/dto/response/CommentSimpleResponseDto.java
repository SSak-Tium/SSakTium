package com.sparta.ssaktium.domain.comments.dto.response;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommentSimpleResponseDto {

    private final Long id;
    private final String content;
    private final LocalDateTime modifiedAt;
    private final int commentLikesCount;

    public CommentSimpleResponseDto(Long id, String content, LocalDateTime modifiedAt, int commentLikesCount) {
        this.id = id;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.commentLikesCount = commentLikesCount;
    }
}
