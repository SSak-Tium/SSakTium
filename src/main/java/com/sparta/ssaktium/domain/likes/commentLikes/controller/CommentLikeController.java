package com.sparta.ssaktium.domain.likes.commentLikes.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikeReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments/{id}/likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<ApiResponse<CommentLikeReponseDto>> postCommentLike (@PathVariable Long id,
                                                                               @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(commentLikeService.postCommentLike(id,authUser.getUserId())));
    }

    // 좋아요 취소
    @DeleteMapping("/{likeId}")
    public void deleteCommentLike(@PathVariable Long id,
                                  @PathVariable Long likeId,
                                  @AuthenticationPrincipal AuthUser authUser){
        commentLikeService.deleteCommentLike(id,likeId,authUser.getUserId());
    }
}
