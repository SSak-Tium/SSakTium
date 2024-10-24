package com.sparta.ssaktium.domain.likes.boardLikes.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikesResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.service.BoardLikesService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/boards/{id}/likes")
public class BoardLikesController {

    private final BoardLikesService boardLikesService;

    // 좋아요 조회
    @GetMapping
    public ResponseEntity<ApiResponse<BoardLikesResponseDto>> getBoardLikes(@PathVariable Long id,
                                                                            @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(boardLikesService.getBoardLikes(id,authUser)));
    }

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<ApiResponse<BoardLikesResponseDto>> postBoardLikes(@PathVariable Long id,
                                                                             @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(boardLikesService.postBoardLikes(id,authUser)));
    }

    // 좋아요 취소
    @DeleteMapping("/{likeId}")
    public void deleteBoardLikes(@PathVariable Long id,
                                 @PathVariable Long likeId,
                                 @AuthenticationPrincipal AuthUser authUser){
        boardLikesService.deleteBoardLikes(id,likeId,authUser);
    }

}
