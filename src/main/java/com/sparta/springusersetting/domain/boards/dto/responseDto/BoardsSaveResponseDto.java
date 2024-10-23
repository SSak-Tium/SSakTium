package com.sparta.springusersetting.domain.boards.dto.responseDto;

import com.sparta.springusersetting.domain.boards.entity.Boards;
import com.sparta.springusersetting.domain.boards.entity.PublicStatus;
import lombok.Getter;

@Getter
public class BoardsSaveResponseDto {

    public final Long id;
    public final String title;
    public final String contents;
    public final String images;

    public BoardsSaveResponseDto(Boards boards) {
        this.id = boards.getId();
        this.title = boards.getTitle();
        this.contents = boards.getContent();
        this.images = boards.getImage();
    }
}
