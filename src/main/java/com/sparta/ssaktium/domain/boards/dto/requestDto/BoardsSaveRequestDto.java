package com.sparta.ssaktium.domain.boards.dto.requestDto;

import com.sparta.ssaktium.domain.boards.entity.PublicStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardsSaveRequestDto {

    private String title;
    private String contents;
    private String images;
    private PublicStatus publicStatus;
}
