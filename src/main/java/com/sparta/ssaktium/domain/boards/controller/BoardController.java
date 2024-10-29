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
     * @param requestDto 게시글 본문작성
     * @param images     게시글 이미지 첨부
     * @return 게시글 저장된 내용 리턴
     */
    @PostMapping("/boards")
    public ResponseEntity<ApiResponse<BoardSaveResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                       @RequestPart BoardSaveRequestDto requestDto,
                                                                       @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(ApiResponse.success(boardService.saveBoards(authUser.getUserId(), requestDto, images)));
    }

    /**
     * 게시글 이미지 수정
     *
     * @param authUser
     * @param id              수정할 게시글 id
     * @param images          게시글 이미지 첨부
     * @param remainingImages 기존에 있던 이미지 리스트
     * @return 수정된 이미지 리스트
     */
    @PostMapping("/boards/{id}/images")
    public ResponseEntity<ApiResponse<BoardUpdateImageDto>> updateImages(@AuthenticationPrincipal AuthUser authUser,
                                                                         @PathVariable Long id,
                                                                         @RequestPart(value = "newimages", required = false) List<MultipartFile> images,
                                                                         @RequestPart(value = "images", required = false) List<String> remainingImages) {
        return ResponseEntity.ok(ApiResponse.success(boardService.updateImages(authUser.getUserId(), id, images, remainingImages)));
    }

    /**
     * 게시글 본문 수정
     *
     * @param authUser
     * @param id         수정할 게시글 id
     * @param requestDto 게시글 본문
     * @return 게시글 전체 내용 리턴
     */
    @PutMapping("/boards/{id}")
    public ResponseEntity<ApiResponse<BoardSaveResponseDto>> updateBoardContent(@AuthenticationPrincipal AuthUser authUser,
                                                                                @PathVariable Long id,
                                                                                @RequestPart BoardSaveRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(boardService.updateBoardContent(authUser.getUserId(), id, requestDto)));
    }

    /**
     * 게시글 삭제상태
     *
     * @param authUser
     * @param id
     * @return status.ok
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
     * @return 해당 게시글 본문 내용
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
     * @param page     볼 페이지
     * @param size     페이지 크기
     * @return 내게시글들
     */
    @GetMapping("/boards")
    public ResponseEntity<ApiResponse<Page<BoardDetailResponseDto>>> getMyBoards(@AuthenticationPrincipal AuthUser authUser,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getMyBoards(authUser.getUserId(), page, size)));
    }

    /**
     * 뉴스피드
     *
     * @param authUser
     * @param page     볼 페이지
     * @param size     페이지 크기
     * @return 나와 내친구들 게시글
     */
    @GetMapping("/newsfeed")
    public ResponseEntity<ApiResponse<Page<BoardDetailResponseDto>>> getNewsfeed(@AuthenticationPrincipal AuthUser authUser,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getNewsfeed(authUser.getUserId(), page, size)));
    }

    /**
     * 전체공개 게시글 조회
     *
     * @param page
     * @param size
     * @return 전체공개 게시글
     */
    @GetMapping("/boards/status-all")
    public ResponseEntity<ApiResponse<Page<BoardDetailResponseDto>>> getAllBoards(@RequestParam(defaultValue = "1") int page,
                                                                                  @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getAllBoards(page, size)));
    }
}
