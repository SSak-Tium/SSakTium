package com.sparta.ssaktium.domain.likes.commentLikes.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikesReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.service.CommentLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/comments/{id}/likes")
public class CommentLikesController {

    private final CommentLikesService commentLikesService;

    // 좋아요 등록
    @PostMapping
    public ResponseEntity<ApiResponse<CommentLikesReponseDto>> postCommentLikes (@PathVariable Long commentId,
                                                                                 @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(ApiResponse.success(commentLikesService.postCommentLikes(commentId,authUser)));
    }
}
