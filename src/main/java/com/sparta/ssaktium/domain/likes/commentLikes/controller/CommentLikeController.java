package com.sparta.ssaktium.domain.likes.commentLikes.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikeReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments/{commentId}/likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<CommonResponse<CommentLikeReponseDto>> postCommentLike (@AuthenticationPrincipal AuthUser authUser,
                                                                                  @PathVariable Long commentId){
        return ResponseEntity.ok(CommonResponse.success(commentLikeService.postCommentLike(commentId,authUser.getUserId())));
    }

    // 좋아요 취소
    @DeleteMapping
    public void deleteCommentLike(@AuthenticationPrincipal AuthUser authUser,
                                  @PathVariable Long commentId){
        commentLikeService.deleteCommentLike(authUser.getUserId(), commentId);
    }
}
