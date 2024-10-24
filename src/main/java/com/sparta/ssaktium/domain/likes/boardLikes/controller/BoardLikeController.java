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
@RequestMapping("/v1/boards/{id}/likes")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    // 좋아요 조회
    @GetMapping
    public ResponseEntity<ApiResponse<BoardLikeResponseDto>> getBoardLikes(@PathVariable Long id,
                                                                           @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(boardLikeService.getBoardLikes(id,authUser)));
    }

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<ApiResponse<BoardLikeResponseDto>> postBoardLikes(@PathVariable Long id,
                                                                            @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(boardLikeService.postBoardLikes(id,authUser)));
    }

    // 좋아요 취소
    @DeleteMapping("/{likeId}")
    public void deleteBoardLikes(@PathVariable Long id,
                                 @PathVariable Long likeId,
                                 @AuthenticationPrincipal AuthUser authUser){
        boardLikeService.deleteBoardLikes(id,likeId,authUser);
    }

}
