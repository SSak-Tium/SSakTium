package com.sparta.ssaktium.domain.likes.boardLikes.dto;

import lombok.Getter;

@Getter
public class BoardLikesResponseDto {
    private final Long boardId;
    private final int boardLikesCount;

    public BoardLikesResponseDto(Long boardId, int boardLikesCount) {
        this.boardId = boardId;
        this.boardLikesCount = boardLikesCount;
    }
}
