package com.sparta.ssaktium.domain.likes.boardLikes.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikesResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.service.BoardLikesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Getter
@RequiredArgsConstructor
@RestController("/v1/boards/{id}/likes")
public class BoardLikesController {

    private final BoardLikesService boardLikesService;

    // 좋아요 조회
    @GetMapping
    public ResponseEntity<ApiResponse<BoardLikesResponseDto>> getBoardLikes(@PathVariable Long boardId,
                                                                            @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(boardLikesService.getBoardLikes(boardId,authUser)));
    }

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<ApiResponse<BoardLikesResponseDto>> postBoardLikes(@PathVariable Long boardId,
                                                                             @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(boardLikesService.postBoardLikes(boardId,authUser)));
    }

    // 좋아요 취소
    @DeleteMapping("/{likeId}")
    public void deleteBoardLikes(@PathVariable Long boardId,
                                 @PathVariable Long likeId,
                                 @AuthenticationPrincipal AuthUser authUser){
        boardLikesService.deleteBoardLikes(boardId,likeId,authUser);
    }

}
