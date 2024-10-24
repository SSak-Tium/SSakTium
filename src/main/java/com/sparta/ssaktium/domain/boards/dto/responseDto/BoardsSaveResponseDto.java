package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Boards;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardsSaveResponseDto {

    public final Long id;
    public final String title;
    public final String contents;
    public final String images;
    public final LocalDateTime createdAt;

    public BoardsSaveResponseDto(Boards boards) {
        this.id = boards.getId();
        this.title = boards.getTitle();
        this.contents = boards.getContent();
        this.images = boards.getImage();
        this.createdAt = boards.getCreatedAt();
    }
}
