package com.sparta.ssaktium.domain.likes.boardLikes.dto;

import lombok.Getter;

@Getter
public class BoardLikeResponseDto {
    private final Long boardId;
    private final int boardLikesCount;

    public BoardLikeResponseDto(Long boardId, int boardLikesCount) {
        this.boardId = boardId;
        this.boardLikesCount = boardLikesCount;
    }
}
