package com.sparta.springusersetting.domain.comments.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.comments.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comments.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comments.service.CommentService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
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
    public ResponseEntity<ApiResponse<Page<CommentResponseDto>>> getComments(@PathVariable Long boardId,
                                                                             @AuthenticationPrincipal AuthUser authUser,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(ApiResponse.success(commentService.getComments(boardId,authUser, page, size)));
    }

    // 댓글 등록
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> postComment(@PathVariable Long boardId,
                                                                       @AuthenticationPrincipal AuthUser authUser,
                                                                       @RequestBody CommentRequestDto commentRequestDto){
        return ResponseEntity.ok(ApiResponse.success(commentService.postComment(boardId,authUser,commentRequestDto)));
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(@PathVariable Long boardId,
                                                                         @PathVariable Long commentId,
                                                                         @AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestBody CommentRequestDto commentRequestDto){
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(boardId,commentId,authUser,commentRequestDto)));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long boardId,
                              @PathVariable Long commentId,
                              @AuthenticationPrincipal AuthUser authUser){
        commentService.deleteComment(boardId,commentId,authUser);
    }
}
