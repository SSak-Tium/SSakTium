package com.sparta.ssaktium.domain.likes.boardLikes.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikeResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.service.BoardLikeService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/boards/{boardId}/likes")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    // 좋아요 조회
    @GetMapping
    public ResponseEntity<ApiResponse<BoardLikeResponseDto>> getBoardLikes(@PathVariable Long boardId){
        return ResponseEntity.ok(ApiResponse.success(boardLikeService.getBoardLikes(boardId)));
    }

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<ApiResponse<BoardLikeResponseDto>> postBoardLikes(@AuthenticationPrincipal AuthUser authUser,
                                                                            @PathVariable Long boardId){
        return ResponseEntity.ok(ApiResponse.success(boardLikeService.postBoardLikes(boardId,authUser.getUserId())));
    }

    // 좋아요 취소
    @DeleteMapping
    public void deleteBoardLikes(@AuthenticationPrincipal AuthUser authUser,
                                 @PathVariable Long boardId){
        boardLikeService.deleteBoardLikes(boardId,authUser.getUserId());
    }

}
