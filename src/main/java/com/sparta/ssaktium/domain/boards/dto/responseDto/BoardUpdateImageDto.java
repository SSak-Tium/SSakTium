package com.sparta.ssaktium.domain.boards.dto.responseDto;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardUpdateImageDto {

    public final List<String> imageList;

    public BoardUpdateImageDto(List<String> imageList) {
        this.imageList = imageList;
    }
}
