package com.sparta.ssaktium.domain.comments.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
@Tag(name = "댓글 기능", description = "댓글 관련 조회, 등록, 수정, 삭제 할 수 있는 기능")
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/{boardId}/comments")
    @Operation(summary = "댓글 요청", description = "댓글 조회할 때 사용하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CommonResponse<Page<CommentResponseDto>>> getComments(@PathVariable
                                                                                @Parameter(description = "게시글 아이디")
                                                                                Long boardId,
                                                                                @RequestParam(defaultValue = "1") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(CommonResponse.success(commentService.getComments(boardId, page, size)));
    }

    // 댓글 등록
    @PostMapping("/{boardId}/comments")
    @Operation(summary = "댓글 등록", description = "댓글을 등록하는 API")
    @ApiResponse(responseCode = "200", description = "댓글이 등록되었습니다.")
    public ResponseEntity<CommonResponse<CommentResponseDto>> postComment(@AuthenticationPrincipal AuthUser authUser,
                                                                          @PathVariable
                                                                          @Parameter(description = "게시글 아이디")
                                                                          Long boardId,
                                                                          @RequestBody
                                                                          @Parameter(description = "댓글 내용")
                                                                          CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(commentService.postComment(authUser.getUserId(), boardId, commentRequestDto)));
    }

    // 댓글 수정
    @PutMapping("/{boardId}/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정하는 API")
    @ApiResponse(responseCode = "200", description = "댓글이 수정되었습니다.")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(@AuthenticationPrincipal AuthUser authUser,
                                                                            @PathVariable
                                                                            @Parameter(description = "게시글 아이디")
                                                                            Long boardId,
                                                                            @PathVariable
                                                                            @Parameter(description = "댓글 아이디")
                                                                            Long commentId,
                                                                            @RequestBody
                                                                            @Parameter(description = "댓글 내용")
                                                                            CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(commentService.updateComment(authUser.getUserId(), boardId, commentId, commentRequestDto)));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "댓글이 삭제되었습니다.")
    public void deleteComment(@AuthenticationPrincipal AuthUser authUser,
                              @PathVariable
                              @Parameter(description = "게시글 아이디")
                              Long commentId) {
        commentService.deleteComment(authUser.getUserId(), commentId);
    }
}
