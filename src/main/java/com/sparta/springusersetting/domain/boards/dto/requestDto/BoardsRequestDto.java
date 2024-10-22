package com.sparta.springusersetting.domain.boards.dto.requestDto;

import com.sparta.springusersetting.domain.boards.entity.PublicStatus;
import lombok.Getter;

@Getter
public class BoardsRequestDto {

    private String title;
    private String contents;
    private String images;
    private PublicStatus publicStatus;
}
