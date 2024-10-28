package com.sparta.ssaktium.domain.plants.plantDiaries.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.PlantDiaryRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.service.PlantDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PlantDiaryController {

    private final PlantDiaryService plantDiaryService;

    /**
     * plantDiary 등록 API
     * @param authUser
     * @param id
     * @param requestDto
     * @param image
     * @return
     * @throws IOException
     */
    @PostMapping("/plants/{id}/diaries")
    public ResponseEntity<ApiResponse<PlantDiaryResponseDto>> createDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                          @PathVariable Long id,
                                                                          @RequestPart PlantDiaryRequestDto requestDto,
                                                                          @RequestPart(required = false) MultipartFile image) {
        PlantDiaryResponseDto responseDto = plantDiaryService.createDiary(authUser.getUserId(), id, requestDto, image);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * plantDiary 목록조회 API
     * @param authUser
     * @param id
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/plants/{id}/diaries")
    public ResponseEntity<ApiResponse<Page<PlantDiaryResponseDto>>> getAllDiaries(@AuthenticationPrincipal AuthUser authUser,
                                                                                  @PathVariable Long id,
                                                                                  @RequestParam(defaultValue = "1") int page,
                                                                                  @RequestParam(defaultValue = "10") int size) {
        Page<PlantDiaryResponseDto> responseDto = plantDiaryService.getAllDiaries(authUser.getUserId(), id, page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * plantDiary 단일조회 API
     * @param authUser
     * @param id
     * @param diaryId
     * @return
     */
    @GetMapping("/plants/{id}/diaries/{diaryId}")
    public ResponseEntity<ApiResponse<PlantDiaryResponseDto>> getDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                       @PathVariable Long id,
                                                                       @PathVariable Long diaryId) {
        PlantDiaryResponseDto responseDto = plantDiaryService.getDiary(authUser.getUserId(), id, diaryId);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * plantDiary 수정 API
     * @param authUser
     * @param id
     * @param diaryId
     * @param requestDto
     * @param image
     * @return
     */
    @PutMapping("/plants/{id}/diaries/{diaryId}")
    public ResponseEntity<ApiResponse<PlantDiaryResponseDto>> updateDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                          @PathVariable Long id,
                                                                          @PathVariable Long diaryId,
                                                                          @RequestPart PlantDiaryRequestDto requestDto,
                                                                          @RequestPart(required = false) MultipartFile image) {
        PlantDiaryResponseDto responseDto = plantDiaryService.updateDiary(authUser.getUserId(), id, diaryId, requestDto, image);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}
