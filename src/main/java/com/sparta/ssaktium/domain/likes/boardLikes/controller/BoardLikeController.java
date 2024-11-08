package com.sparta.ssaktium.domain.likes.boardLikes.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikeResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.service.BoardLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/boards/{boardId}/likes")
@Tag(name = "게시글 좋아요 기능", description = "게시글에 좋아요를 등록,취소 할 수 있는 기능")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    // 좋아요 등록
    @PostMapping
    @Operation(summary = "게시글 좋아요 등록", description = "게시글에 좋아요를 등록하는 API")
    @ApiResponse(responseCode = "200", description = "게시글에 좋아요를 눌렀습니다.")
    public ResponseEntity<CommonResponse<BoardLikeResponseDto>> postBoardLikes(@AuthenticationPrincipal AuthUser authUser,
                                                                               @PathVariable
                                                                               @Parameter(description = "게시글 아이디")
                                                                               Long boardId) {
        return ResponseEntity.ok(CommonResponse.success(boardLikeService.postBoardLikes(authUser.getUserId(), boardId)));
    }

    // 좋아요 취소
    @DeleteMapping
    @Operation(summary = "게시글 좋아요 취소", description = "게시글에 좋아요를 취소하는 API")
    @ApiResponse(responseCode = "200", description = "게시글에 좋아요를 취소했습니다.")
    public void deleteBoardLikes(@AuthenticationPrincipal AuthUser authUser,
                                 @PathVariable
                                 @Parameter(description = "게시글 아이디")
                                 Long boardId) {
        boardLikeService.deleteBoardLikes(authUser.getUserId(), boardId);
    }

}
