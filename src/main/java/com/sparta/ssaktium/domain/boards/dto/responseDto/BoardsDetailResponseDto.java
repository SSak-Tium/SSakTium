package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Boards;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardsDetailResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final String images;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public BoardsDetailResponseDto(Boards boards) {
        this.id = boards.getId();
        this.title = boards.getTitle();
        this.contents = boards.getContent();
        this.images = boards.getImage();
        this.createdAt = boards.getCreatedAt();
        this.modifiedAt = boards.getModifiedAt();
    }
}
