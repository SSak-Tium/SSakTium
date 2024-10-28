package com.sparta.ssaktium.domain.boards.dto.responseDto;

import com.sparta.ssaktium.domain.boards.entity.Board;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class BoardSaveResponseDto {

    public final Long id;
    public final String title;
    public final String contents;
    public final List<String> imageList;

    public BoardSaveResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.imageList = board.getImageList();
    }
}
