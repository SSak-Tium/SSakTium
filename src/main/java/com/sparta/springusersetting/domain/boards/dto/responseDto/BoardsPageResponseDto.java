package com.sparta.springusersetting.domain.boards.dto.responseDto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardsPageResponseDto {

    private final List<BoardsDetailResponseDto> content;
    private final int totalPages;
    private final long totalElements;
    private final int size;
    private final int number;

    public BoardsPageResponseDto(List<BoardsDetailResponseDto> content, int totalPages, long totalElements, int size, int number) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
    }
}
