package com.sparta.springusersetting.domain.boards.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.boards.dto.requestDto.BoardsRequestDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsResponseDto;
import com.sparta.springusersetting.domain.boards.service.BoardsService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardsResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                 @RequestBody BoardsRequestDto requestDto){
        return  ResponseEntity.ok(ApiResponse.success(boardsService.saveBoard(authUser,requestDto)));
    }
}
