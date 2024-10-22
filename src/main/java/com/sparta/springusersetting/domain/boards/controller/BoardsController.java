package com.sparta.springusersetting.domain.boards.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsDetailResponseDto;
import com.sparta.springusersetting.domain.boards.dto.requestDto.BoardsSaveRequestDto;
import com.sparta.springusersetting.domain.boards.dto.responseDto.BoardsSaveResponseDto;
import com.sparta.springusersetting.domain.boards.service.BoardsService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping()
    public ResponseEntity<ApiResponse<BoardsSaveResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                        @RequestBody BoardsSaveRequestDto requestDto){
        return  ResponseEntity.ok(ApiResponse.success(boardsService.saveBoards(authUser,requestDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardsSaveResponseDto>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                           @PathVariable Long id,
                                                                           @RequestBody BoardsSaveRequestDto requestDto){
        return ResponseEntity.ok(ApiResponse.success(boardsService.updateBoards(authUser,id,requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                       @PathVariable Long id){
        boardsService.deleteBoards(authUser,id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardsDetailResponseDto>> getBoard(@PathVariable Long id){
        boardsService.getBoard();
    }
}
