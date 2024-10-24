package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import lombok.Getter;

@Getter
public class BoardSaveResponseDto {

    public final Long id;
    public final String title;
    public final String contents;
    public final String images;

    public BoardSaveResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.images = board.getImage();
    }
}
