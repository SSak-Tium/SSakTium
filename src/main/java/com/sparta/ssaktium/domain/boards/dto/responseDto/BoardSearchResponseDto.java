package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardSearchResponseDto {

    private final Long id;
    private final String title;
    private final String contents;

    public BoardSearchResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
    }
}
