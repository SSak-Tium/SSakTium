package com.sparta.springusersetting.domain.boards.dto.responseDto;

import com.sparta.springusersetting.domain.boards.entity.Boards;
import com.sparta.springusersetting.domain.boards.entity.PublicStatus;
import lombok.Getter;

@Getter
public class BoardsResponseDto {

    public final String title;
    public final String contents;
    public final String images;
    public final PublicStatus publicStatus;

    public BoardsResponseDto(Boards boards) {
        this.title = boards.getTitle();
        this.contents = boards.getContent();
        this.images = boards.getImage();
        this.publicStatus = boards.getPublicStatus();
    }
}
