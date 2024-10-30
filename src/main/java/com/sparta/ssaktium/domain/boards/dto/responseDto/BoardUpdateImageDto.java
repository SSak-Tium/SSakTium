package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.BoardImages;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardUpdateImageDto {

    public final List<String> imageUrls;

    public BoardUpdateImageDto(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
