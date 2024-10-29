package com.sparta.ssaktium.domain.comments.controller;

import com.sparta.ssaktium.config.CommonResponse;
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
@RequestMapping("/v1/boards")
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<CommonResponse<Page<CommentResponseDto>>> getComments(@PathVariable Long boardId,
                                                                                @RequestParam(defaultValue = "1") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(CommonResponse.success(commentService.getComments(boardId, page, size)));
    }

    // 댓글 등록
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postComment(@AuthenticationPrincipal AuthUser authUser,
                                                                          @PathVariable Long boardId,
                                                                          @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(commentService.postComment(authUser.getUserId(), boardId, commentRequestDto)));
    }

    // 댓글 수정
    @PutMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(@AuthenticationPrincipal AuthUser authUser,
                                                                            @PathVariable Long boardId,
                                                                            @PathVariable Long commentId,
                                                                            @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(commentService.updateComment(authUser.getUserId(), boardId, commentId, commentRequestDto)));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@AuthenticationPrincipal AuthUser authUser,
                              @PathVariable Long commentId) {
        commentService.deleteComment(authUser.getUserId(), commentId);
    }
}
