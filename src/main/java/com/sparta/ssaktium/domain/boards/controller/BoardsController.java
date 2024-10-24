package com.sparta.ssaktium.domain.boards.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardsSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardsPageResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardsSaveResponseDto;
import com.sparta.ssaktium.domain.boards.service.BoardsService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardsController {

    private final BoardsService boardsService;

    /**
     * 게시글 작성
     * @param authUser
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardsSaveResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                        @RequestBody BoardsSaveRequestDto requestDto){
        return  ResponseEntity.ok(ApiResponse.success(boardsService.saveBoards(authUser,requestDto)));
    }

    /**
     * 게시글 수정
     * @param authUser
     * @param id
     * @param requestDto
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardsSaveResponseDto>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                           @PathVariable Long id,
                                                                           @RequestBody BoardsSaveRequestDto requestDto){
        return ResponseEntity.ok(ApiResponse.success(boardsService.updateBoards(authUser,id,requestDto)));
    }

    /**
     * 게시글 삭제변환
     * @param authUser
     * @param id
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                       @PathVariable Long id){
        boardsService.deleteBoards(authUser,id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    //게시글 단건 조회 (댓글 빠져있음)
//    @GetMapping("/boards/{id}")
//    public ResponseEntity<ApiResponse<BoardsDetailResponseDto>> getBoard(@PathVariable Long id){
//        boardsService.getBoard();
//        return ResponseEntity.ok(ApiResponse.success(boardsService.getBoard()));
//    }

    /**
     * 내 게시글 조회
     * @param authUser
     * @param page
     * @param size
     * 댓글 페이지네이션 빠져있음
     */
    @GetMapping("/boards/me")
    public ResponseEntity<ApiResponse<BoardsPageResponseDto>> getMyBoards(@AuthenticationPrincipal AuthUser authUser,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(ApiResponse.success(boardsService.getMyBoards(authUser,page,size)));
    }

    /**
     * 친구항목 필요
     * @param authUser
     * @param page
     * @param size
     */
//    @GetMapping("/newsfeed")
//    public ResponseEntity<ApiResponse<Page<BoardsDetailResponseDto>>> getNewsfeed(@AuthenticationPrincipal AuthUser authUser,
//                                                                                  @RequestParam(defaultValue = "1") int page,
//                                                                                  @RequestParam(defaultValue = "5") int size){
//        return ResponseEntity.ok(ApiResponse.success(boardsService.getNewsfeed(authUser,page,size)));
//    }

}
