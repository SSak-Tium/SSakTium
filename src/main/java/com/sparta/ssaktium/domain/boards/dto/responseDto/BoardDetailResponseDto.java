package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.entity.BoardImages;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardDetailResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final int boardLikesCount;
    private final List<BoardImages> imageUrls;
    private final int commentCount;

    public BoardDetailResponseDto(Board board, int commentCount) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.boardLikesCount = board.getBoardLikesCount();
        this.imageUrls = board.getImageUrls();
        this.commentCount = commentCount;
    }
}
