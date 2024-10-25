package com.sparta.ssaktium.domain.comments.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards/{id}")
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<Page<CommentResponseDto>>> getComments(@PathVariable Long id,
                                                                             @AuthenticationPrincipal AuthUser authUser,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(ApiResponse.success(commentService.getComments(id,authUser.getUserId(), page, size)));
    }

    // 댓글 등록
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> postComment(@PathVariable Long id,
                                                                       @AuthenticationPrincipal AuthUser authUser,
                                                                       @RequestBody CommentRequestDto commentRequestDto){
        return ResponseEntity.ok(ApiResponse.success(commentService.postComment(id,authUser.getUserId(),commentRequestDto)));
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(@PathVariable Long id,
                                                                         @PathVariable Long commentId,
                                                                         @AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestBody CommentRequestDto commentRequestDto){
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(id,commentId,authUser.getUserId(),commentRequestDto)));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long id,
                              @PathVariable Long commentId,
                              @AuthenticationPrincipal AuthUser authUser){
        commentService.deleteComment(id,commentId,authUser.getUserId());
    }
}
