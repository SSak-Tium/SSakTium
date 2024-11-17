package com.sparta.ssaktium.domain.likes.commentLikes.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikeReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.service.CommentLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments/{commentId}/likes")
@Tag(name = "댓글 좋아요 기능", description = "댓글에 좋아요를 등록,취소 할 수 있는 기능")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 좋아요 등록
    @PostMapping
    @Operation(summary = "댓글 좋아요 등록", description = "댓글에 좋아요를 등록하는 API")
    @ApiResponse(responseCode = "200", description = "댓글에 좋아요를 눌렀습니다.")
    public ResponseEntity<CommonResponse<CommentLikeReponseDto>> postCommentLike(@AuthenticationPrincipal AuthUser authUser,
                                                                                 @PathVariable
                                                                                 @Parameter(description = "댓글 아이디")
                                                                                 Long commentId) {
        return ResponseEntity.ok(CommonResponse.success(commentLikeService.postCommentLike(authUser.getUserId(), commentId)));
    }

    // 좋아요 취소
    @DeleteMapping
    @Operation(summary = "댓글 좋아요 취소", description = "댓글에 좋아요를 취소하는 API")
    @ApiResponse(responseCode = "200", description = "댓글에 좋아요를 취소했습니다.")
    public void deleteCommentLike(@AuthenticationPrincipal AuthUser authUser,
                                  @PathVariable
                                  @Parameter(description = "댓글 아이디")
                                  Long commentId) {
        commentLikeService.deleteCommentLike(authUser.getUserId(), commentId);
    }
}
