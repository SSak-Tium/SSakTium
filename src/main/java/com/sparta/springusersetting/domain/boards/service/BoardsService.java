package com.sparta.springusersetting.domain.boards.service;

import com.sparta.springusersetting.domain.boards.dto.requestDto.BoardsRequestDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsResponseDto;
import com.sparta.springusersetting.domain.boards.entity.Boards;
import com.sparta.springusersetting.domain.boards.repository.BoardsRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;
    private final UserService userService;

    @Transactional
    public BoardsResponseDto saveBoard(AuthUser authUser, BoardsRequestDto requestDto) {
        //유저 찾기
        userService.findUser(authUser.getUserId());
        //제공받은 정보로 새 보드 만들기
        Boards newBoard = new Boards(requestDto);
        //저장
        boardsRepository.save(newBoard);

        return new BoardsResponseDto(newBoard);
    }
}
