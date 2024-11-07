package com.sparta.ssaktium.domain.boards.controller;


import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.boards.dto.requestDto.BoardSaveRequestDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardDetailResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSaveResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardSearchResponseDto;
import com.sparta.ssaktium.domain.boards.dto.responseDto.BoardUpdateImageDto;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "게시판 기능", description = "게시판 생성,수정,삭제,조회 등")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    @Operation(summary = "게시판 생성", description = "게시판 생성하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<BoardSaveResponseDto>> saveBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                          @RequestPart
                                                                          @Parameter(description = "게시판 입력 정보")
                                                                          BoardSaveRequestDto requestDto,
                                                                          @RequestPart(value = "image", required = false)
                                                                          @Parameter(description = "게시판 이미지")
                                                                          List<MultipartFile> images) {
        return ResponseEntity.ok(CommonResponse.success(boardService.saveBoards(authUser.getUserId(), requestDto, images)));
    }


    @PostMapping("/boards/{id}/images")
    @Operation(summary = "게시판 이미지 수정", description = "게시판 이미지 수정하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<BoardUpdateImageDto>> updateImages(@AuthenticationPrincipal AuthUser authUser,
                                                                            @PathVariable
                                                                            @Parameter(description = "수정할 게시판 아이디")
                                                                            Long id,
                                                                            @RequestPart(value = "newimages", required = false)
                                                                            @Parameter(description = "추가할 이미지")
                                                                            List<MultipartFile> images,
                                                                            @RequestPart(value = "images", required = false)
                                                                            @Parameter(description = "남길 이미지")
                                                                            List<String> remainingImages) {
        return ResponseEntity.ok(CommonResponse.success(boardService.updateImages(authUser.getUserId(), id, images, remainingImages)));
    }

    @PutMapping("/boards/{id}")
    @Operation(summary = "게시판 본문 수정", description = "게시판 본문 수정하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<BoardSaveResponseDto>> updateBoardContent(@AuthenticationPrincipal AuthUser authUser,
                                                                                   @PathVariable
                                                                                   @Parameter(description = "수정할 게시판 아이디")
                                                                                   Long id,
                                                                                   @RequestPart
                                                                                   @Parameter(description = "게시판 입력 정보")
                                                                                   BoardSaveRequestDto requestDto) {
        return ResponseEntity.ok(CommonResponse.success(boardService.updateBoardContent(authUser.getUserId(), id, requestDto)));
    }


    @DeleteMapping("/boards/{id}")
    @Operation(summary = "게시판 삭제 요청", description = "게시판 삭제 상태로 변경하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                            @PathVariable
                                                            @Parameter(description = "삭제할 게시판 아이디")
                                                            Long id) {
        boardService.deleteBoards(authUser.getUserId(), id);
        return ResponseEntity.ok(CommonResponse.success(null));
    }

    @GetMapping("/boards/{id}")
    @Operation(summary = "게시판 단건 조회", description = "게시판 단건 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<BoardDetailResponseDto>> getBoard(@PathVariable
                                                                           @Parameter(description = "조회할 게시판 아이디")
                                                                           Long id) {
        return ResponseEntity.ok(CommonResponse.success(boardService.getBoard(id)));
    }

    @GetMapping("/boards")
    @Operation(summary = "나 또는 전체 게시판 조회", description = "게시판 전체 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<Page<BoardDetailResponseDto>>> getBoards(@AuthenticationPrincipal AuthUser authUser,
                                                                                  @RequestParam(defaultValue = "all")
                                                                                  @Parameter(description = "조회하고싶은 게시판 타입", example = "me or all")
                                                                                  String type,
                                                                                  @RequestParam(defaultValue = "1")
                                                                                  @Parameter(description = "조회하고싶은 페이지")
                                                                                  int page,
                                                                                  @RequestParam(defaultValue = "5")
                                                                                  @Parameter(description = "한페이지에 나올 게시글 수")
                                                                                  int size) {

        Long userId = (type.equals("all")) ? null : authUser.getUserId(); // "me"일 때만 userId 사용
        return ResponseEntity.ok(CommonResponse.success(boardService.getBoards(userId, type, page, size)));
    }


    @GetMapping("/newsfeed")
    @Operation(summary = "뉴스피드 조회", description = "뉴스피드 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<Page<BoardDetailResponseDto>>> getNewsfeed(@AuthenticationPrincipal AuthUser authUser,
                                                                                    @RequestParam(defaultValue = "1")
                                                                                    @Parameter(description = "조회하고싶은 페이지")
                                                                                    int page,
                                                                                    @RequestParam(defaultValue = "5")
                                                                                    @Parameter(description = "한페이지에 나올 게시글 수")
                                                                                    int size) {
        return ResponseEntity.ok(CommonResponse.success(boardService.getNewsfeed(authUser.getUserId(), page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<BoardSearchResponseDto>>> searchBoard(@RequestParam String keyword) {
        return ResponseEntity.ok(CommonResponse.success(boardService.searchBoard(keyword)));
    }
}
