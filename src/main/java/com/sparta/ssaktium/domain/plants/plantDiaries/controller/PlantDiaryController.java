package com.sparta.ssaktium.domain.plants.plantDiaries.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.PlantDiaryRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.service.PlantDiaryService;
import lombok.Getter;
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
                                                                          @RequestPart(required = false) MultipartFile image) throws IOException {
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

}
