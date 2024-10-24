package com.sparta.ssaktium.domain.boards.dto.responseDto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardPageResponseDto {

    private final List<BoardDetailResponseDto> content;
    private final int totalPages;
    private final long totalElements;
    private final int size;
    private final int number;

    public BoardPageResponseDto(List<BoardDetailResponseDto> content, int totalPages, long totalElements, int size, int number) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
    }
}
