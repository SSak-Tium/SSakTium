package com.sparta.ssaktium.domain.plants.plantDiaries.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryUpdateRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.service.PlantDiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "내 식물 다이어리 관리기능", description = "내 식물의 다이어리(활동)을 등록하고 관리할 수 있는 기능")
public class PlantDiaryController {

    private final PlantDiaryService plantDiaryService;

    @PostMapping("/plants/{id}/diaries")
    @Operation(summary = "내 식물 등록", description = "내 식물 등록하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<PlantDiaryResponseDto>> createDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                             @PathVariable
                                                                             @Parameter(description = "식물아이디")
                                                                             Long id,
                                                                             @RequestPart
                                                                             @Parameter(description = "식물정보")
                                                                             PlantDiaryRequestDto requestDto,
                                                                             @RequestPart(required = false)
                                                                             @Parameter(description = "식물이미지파일 (선택)")
                                                                             MultipartFile image) {
        PlantDiaryResponseDto responseDto = plantDiaryService.createDiary(authUser.getUserId(), id, requestDto, image);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @GetMapping("/plants/{id}/diaries")
    @Operation(summary = "내 식물 다이어리 다건 조회", description = "내 식물 다이어리를 다건 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<Page<PlantDiaryResponseDto>>> getAllDiaries(@AuthenticationPrincipal AuthUser authUser,
                                                                                     @PathVariable
                                                                                     @Parameter(description = "식물아이디")
                                                                                     Long id,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<PlantDiaryResponseDto> responseDto = plantDiaryService.getAllDiaries(authUser.getUserId(), id, page, size);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @GetMapping("/plants/{plantId}/diaries/{diaryId}")
    @Operation(summary = "내 식물 다이어리 단건 조회", description = "내 식물 다이어리를 단건 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    @Parameters({
            @Parameter(description = "식물아이디"),
            @Parameter(description = "식물다이어리 아이디")
    })
    public ResponseEntity<CommonResponse<PlantDiaryResponseDto>> getDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                          @PathVariable Long plantId,
                                                                          @PathVariable Long diaryId) {
        PlantDiaryResponseDto responseDto = plantDiaryService.getDiary(authUser.getUserId(), plantId, diaryId);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @PutMapping("/plants/{plantId}/diaries/{diaryId}")
    @Operation(summary = "내 식물 다이어리 수정", description = "내 식물 다이어리를 다건 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<PlantDiaryResponseDto>> updateDiary(@AuthenticationPrincipal AuthUser authUser,
                                                                             @PathVariable
                                                                             @Parameter(description = "식물 아이디")
                                                                             Long plantId,
                                                                             @PathVariable
                                                                             @Parameter(description = "식물 다이어리 아이디")
                                                                             Long diaryId,
                                                                             @RequestBody
                                                                             @Parameter(description = "식물 다이어리 정보")
                                                                             PlantDiaryUpdateRequestDto requestDto) {
        PlantDiaryResponseDto responseDto = plantDiaryService.updateDiary(authUser.getUserId(), plantId, diaryId, requestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @DeleteMapping("/plants/{plantId}/diaries/{diaryId}")
    @Operation(summary = "내 식물 다이어리 삭제", description = "내 식물 다이어리를 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<Void>> deleteDiary(@AuthenticationPrincipal AuthUser authUser,
                                                              @PathVariable
                                                              @Parameter(description = "식물 아이디")
                                                              Long plantId,
                                                              @PathVariable
                                                              @Parameter(description = "식물 다이어리 아이디")
                                                              Long diaryId) {
        plantDiaryService.deleteDiary(authUser.getUserId(), plantId, diaryId);
        return ResponseEntity.ok(CommonResponse.success(null));
    }

    @PostMapping("/diaries/image")
    @Operation(summary = "다이어리 수정 이미지 등록", description = "수정할 이미지 등록하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<String>> uploadDiaryImage(@RequestPart
                                                                   @Parameter (description = "수정할 이미지")
                                                                   MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(plantDiaryService.uploadDiaryImage(image)));
    }
}
