package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.comments.dto.response.CommentSimpleResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardDetailResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final List<String> imageUrl;
    private final int boardLikesCount;
    private final List<CommentSimpleResponseDto> comments;

    public BoardDetailResponseDto(Board board,List<CommentSimpleResponseDto> comments) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.imageUrl = board.getImageList();
        this.boardLikesCount = board.getBoardLikesCount();
        this.comments = comments;
    }
}
