package com.sparta.springusersetting.domain.boards.entity;

import com.sparta.springusersetting.domain.boards.dto.requestDto.BoardsRequestDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsResponseDto;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Boards extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Enumerated
    private PublicStatus publicStatus;

    public Boards (BoardsRequestDto boardsRequestDto){
        this.title = boardsRequestDto.getTitle();
        this.content = boardsRequestDto.getContents();
        this.image = boardsRequestDto.getImages();
        this.publicStatus = boardsRequestDto.getPublicStatus();
    }
}
