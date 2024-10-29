package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.entity.BoardImages;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardSaveResponseDto {

    public final Long id;
    public final String title;
    public final String contents;
    private final List<BoardImages> imageUrls;

    public BoardSaveResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.imageUrls = board.getImageUrls();
    }
}
