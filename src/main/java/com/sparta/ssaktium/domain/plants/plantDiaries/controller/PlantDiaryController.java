package com.sparta.ssaktium.domain.plants.plantDiaries.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryUpdateRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.service.PlantDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PlantDiaryController {

    private final PlantDiaryService plantDiaryService;

    /**
     * plantDiary 등록 API
     *
     * @param authUser
     * @param id
     * @param requestDto
     * @param image
     * @return
     */
    @PostMapping("/plants/{id}/diaries")
    public ResponseEntity<CommonResponse<PlantDiaryResponseDto>> createDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                             @PathVariable Long id,
                                                                             @RequestPart PlantDiaryRequestDto requestDto,
                                                                             @RequestPart(required = false) MultipartFile image) {
        PlantDiaryResponseDto responseDto = plantDiaryService.createDiary(authUser.getUserId(), id, requestDto, image);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * plantDiary 목록조회 API
     *
     * @param authUser
     * @param id
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/plants/{id}/diaries")
    public ResponseEntity<CommonResponse<Page<PlantDiaryResponseDto>>> getAllDiaries(@AuthenticationPrincipal AuthUser authUser,
                                                                                     @PathVariable Long id,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<PlantDiaryResponseDto> responseDto = plantDiaryService.getAllDiaries(authUser.getUserId(), id, page, size);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * plantDiary 단일조회 API
     *
     * @param authUser
     * @param id
     * @param diaryId
     * @return
     */
    @GetMapping("/plants/{id}/diaries/{diaryId}")
    public ResponseEntity<CommonResponse<PlantDiaryResponseDto>> getDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                          @PathVariable Long id,
                                                                          @PathVariable Long diaryId) {
        PlantDiaryResponseDto responseDto = plantDiaryService.getDiary(authUser.getUserId(), id, diaryId);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * plantDiary 수정 API
     *
     * @param authUser
     * @param id
     * @param diaryId
     * @param requestDto
     * @return
     */
    @PutMapping("/plants/{id}/diaries/{diaryId}")
    public ResponseEntity<CommonResponse<PlantDiaryResponseDto>> updateDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                             @PathVariable Long id,
                                                                             @PathVariable Long diaryId,
                                                                             @RequestPart PlantDiaryUpdateRequestDto requestDto) {
        PlantDiaryResponseDto responseDto = plantDiaryService.updateDiary(authUser.getUserId(), id, diaryId, requestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * plantDiary 삭제 API
     *
     * @param authUser
     * @param id
     * @param diaryId
     * @return
     */
    @DeleteMapping("/plants/{id}/diaries/{diaryId}")
    public ResponseEntity<CommonResponse<String>> deleteDiary(@AuthenticationPrincipal AuthUser authUser,
                                                              @PathVariable Long id,
                                                              @PathVariable Long diaryId) {
        return ResponseEntity.ok(CommonResponse.success(plantDiaryService.deleteDiary(authUser.getUserId(), id, diaryId)));
    }

    /**
     * 수정 이미지 등록 API
     *
     * @param authUser
     * @param image
     * @return
     */
    @PostMapping("/diaries/image")
    public ResponseEntity<CommonResponse<String>> uploadDiaryImage(@AuthenticationPrincipal AuthUser authUser,
                                                                   @RequestParam MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(plantDiaryService.uploadDiaryImage(authUser.getUserId(), image)));
    }
}
