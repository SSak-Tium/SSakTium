package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import lombok.Getter;

@Getter
public class BoardDetailResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final String images;

    public BoardDetailResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.images = board.getImage();
    }
}
