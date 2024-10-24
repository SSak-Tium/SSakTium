package com.sparta.ssaktium.domain.boards.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardPageResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     * @param authUser
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardSaveResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                       @RequestBody BoardSaveRequestDto requestDto){
        return  ResponseEntity.ok(ApiResponse.success(boardService.saveBoards(authUser,requestDto)));
    }

    /**
     * 게시글 수정
     * @param authUser
     * @param id
     * @param requestDto
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardSaveResponseDto>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                         @PathVariable Long id,
                                                                         @RequestBody BoardSaveRequestDto requestDto){
        return ResponseEntity.ok(ApiResponse.success(boardService.updateBoards(authUser,id,requestDto)));
    }

    /**
     * 게시글 삭제변환
     * @param authUser
     * @param id
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                       @PathVariable Long id){
        boardService.deleteBoards(authUser,id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    /**
     * 게시글 단건 조회
     * @param id
     * @return
     */
    @GetMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardDetailResponseDto>> getBoard(@PathVariable Long id){
        boardService.getBoard();
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoard()));
    }

    /**
     * 내 게시글 조회
     * @param authUser
     * @param page
     * @param size
     * 댓글 페이지네이션 빠져있음
     */
    @GetMapping("/boards/me")
    public ResponseEntity<ApiResponse<BoardPageResponseDto>> getMyBoards(@AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestParam(defaultValue = "1") int page,
                                                                         @RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(ApiResponse.success(boardService.getMyBoards(authUser,page,size)));
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
