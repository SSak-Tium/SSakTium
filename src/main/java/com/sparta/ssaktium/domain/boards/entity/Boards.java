package com.sparta.ssaktium.domain.boards.entity;

import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardsSaveRequestDto;
import com.sparta.ssaktium.domain.boards.enums.PublicStatus;
import com.sparta.ssaktium.domain.boards.enums.StatusEnum;
import com.sparta.ssaktium.domain.common.entity.Timestamped;
import com.sparta.ssaktium.domain.users.entity.Users;
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
    private Users user;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum;

    public Boards (BoardsSaveRequestDto boardsSaveRequestDto,Users user){
        this.title = boardsSaveRequestDto.getTitle();
        this.content = boardsSaveRequestDto.getContents();
        this.image = boardsSaveRequestDto.getImages();
        this.publicStatus = boardsSaveRequestDto.getPublicStatus();
        this.user = user;
        this.statusEnum = StatusEnum.ACTIVATED;
    }

    public void updateBoards(BoardsSaveRequestDto boardsSaveRequestDto){
        this.title = boardsSaveRequestDto.getTitle();
        this.content = boardsSaveRequestDto.getContents();
        this.image = boardsSaveRequestDto.getImages();
        this.publicStatus = boardsSaveRequestDto.getPublicStatus();
        this.statusEnum = StatusEnum.ACTIVATED;
    }

    public void deleteBoards(){
        this.statusEnum = StatusEnum.DELETED;
    }
}
