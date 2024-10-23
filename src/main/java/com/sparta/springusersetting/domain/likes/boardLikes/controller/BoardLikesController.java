package com.sparta.springusersetting.domain.likes.boardLikes.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.likes.boardLikes.dto.BoardLikesResponseDto;
import com.sparta.springusersetting.domain.likes.boardLikes.service.BoardLikesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RequiredArgsConstructor
@RestController("/v1/boards/{id}/likes")
public class BoardLikesController {

    private final BoardLikesService boardLikesService;

    // 좋아요 조회
    @GetMapping
    public ResponseEntity<ApiResponse<BoardLikesResponseDto>> getBoardLikes(@PathVariable Long boardId){
        return ResponseEntity.ok(ApiResponse.success(boardLikesService.getBoardLikes(boardId)));
    }

}
