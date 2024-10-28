package com.sparta.ssaktium.domain.boards.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardUpdateImageDto;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     *
     * @param authUser
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardSaveResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                       @RequestPart BoardSaveRequestDto requestDto,
                                                                       @RequestPart(value = "image", required = false) List<MultipartFile> images)  {
        return ResponseEntity.ok(ApiResponse.success(boardService.saveBoards(authUser.getUserId(), requestDto, images)));
    }

    /**
     * 게시글 이미지 수정
     * @param authUser
     * @param id
     */
    @PostMapping("/boards/{id}/images")
    public ResponseEntity<ApiResponse<BoardUpdateImageDto>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                        @PathVariable Long id,
                                                                        @RequestPart(value = "newimages", required = false) List<MultipartFile> images,
                                                                        @RequestPart(value = "images", required = false) List<String> remainingImages) {
        return ResponseEntity.ok(ApiResponse.success(boardService.updateImagesBoards(authUser.getUserId(), id, images,remainingImages)));
    }

    /**
     * 게시글 내용 수정
     * @param authUser
     * @param id
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardSaveResponseDto>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                         @PathVariable Long id,
                                                                         @RequestPart BoardSaveRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(boardService.updateBoardContent(authUser.getUserId(), id,requestDto)));
    }

    /**
     * 게시글 삭제변환
     *
     * @param authUser
     * @param id
     */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                         @PathVariable Long id) {
        boardService.deleteBoards(authUser.getUserId(), id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    /**
     * 게시글 단건 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardDetailResponseDto>> getBoard(@PathVariable Long id) {
        boardService.getBoard(id);
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoard(id)));
    }

    /**
     * 내 게시글 조회
     *
     * @param authUser
     * @param page
     * @param size     댓글 페이지네이션 빠져있음
     */
    @GetMapping("/boards")
    public ResponseEntity<ApiResponse<Page<BoardDetailResponseDto>>> getMyBoards(@AuthenticationPrincipal AuthUser authUser,
                                                                         @RequestParam(defaultValue = "1") int page,
                                                                         @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getMyBoards(authUser.getUserId(), page, size)));
    }

    /**
     * 뉴스피드
     * @param authUser
     * @param page
     * @param size
     */
    @GetMapping("/newsfeed")
    public ResponseEntity<ApiResponse<Page<BoardDetailResponseDto>>> getNewsfeed(@AuthenticationPrincipal AuthUser authUser,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getNewsfeed(authUser.getUserId(), page, size)));
    }

    /**
     * 게시글 전체 조회
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/boards/status-all")
    public ResponseEntity<ApiResponse<Page<BoardDetailResponseDto>>> getAllBoards(@RequestParam(defaultValue = "1") int page,
                                                                                  @RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(ApiResponse.success(boardService.getAllBoards(page,size)));
    }
}
