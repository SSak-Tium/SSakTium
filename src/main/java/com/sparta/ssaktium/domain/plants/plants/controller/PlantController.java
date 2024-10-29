package com.sparta.ssaktium.domain.plants.plants.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.plants.dto.requestDto.PlantRequestDto;
import com.sparta.ssaktium.domain.plants.plants.dto.requestDto.PlantUpdateRequestDto;
import com.sparta.ssaktium.domain.plants.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.plants.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "내 식물 관리기능", description = "내 식물을 등록하고 관리할 수 있는 기능")
public class PlantController {

    private final PlantService plantService;

    @PostMapping("/plants")
    @Operation(summary = "내 식물 등록", description = "내 식물 등록하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<PlantResponseDto>> createPlant(@AuthenticationPrincipal AuthUser authUser,
                                                                        @RequestPart @Parameter(description = "식물정보")
                                                                        PlantRequestDto requestDto,
                                                                        @RequestPart(required = false) @Parameter(description = "식물이미지파일 (선택)")
                                                                        MultipartFile image) {
        PlantResponseDto responseDto = plantService.createPlant(authUser.getUserId(), requestDto, image);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @GetMapping("/plants/{id}")
    @Operation(summary = "내 식물 단건 조회", description = "내 식물 단건조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    @Parameter(description = "내 식물 아이디", example = "1")
    public ResponseEntity<CommonResponse<PlantResponseDto>> getPlant(@AuthenticationPrincipal AuthUser authUser,
                                                                     @PathVariable Long id) {

        PlantResponseDto responseDto = plantService.getPlant(authUser.getUserId(), id);
        return ResponseEntity.ok(CommonResponse.success(responseDto));

    }

    @GetMapping("/plants")
    @Operation(summary = "내 식물 다건 조회", description = "내 식물 다건조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<List<PlantResponseDto>>> getAllPlants(@AuthenticationPrincipal AuthUser authUser) {
        List<PlantResponseDto> responseDto = plantService.getAllPlants(authUser.getUserId());
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @PutMapping("/plants/{id}")
    @Operation(summary = "내 식물 수정", description = "내 식물 수정하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<PlantResponseDto>> updatePlant(@AuthenticationPrincipal AuthUser authUser,
                                                                        @PathVariable
                                                                        @Parameter(description = "내 식물 아이디", example = "1")
                                                                        Long id,
                                                                        @RequestPart
                                                                        @Parameter(description = "식물정보")
                                                                        PlantUpdateRequestDto requestDto) {
        PlantResponseDto responseDto = plantService.updatePlant(authUser.getUserId(), id, requestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    @DeleteMapping("/plants/{id}")
    @Operation(summary = "내 식물 삭제", description = "내 식물 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    @Parameter(description = "내 식물 아이디", example = "2")
    public ResponseEntity<CommonResponse<String>> deletePlant(@AuthenticationPrincipal AuthUser authUser,
                                                              @PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.success(plantService.deletePlant(authUser.getUserId(), id)));
    }

    @PostMapping("/plants/image")
    @Operation(summary = "식물 수정이미지 업로드", description = "식물 정보 수정 시 이미지업로드용 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<String>> uploadPlantImage(@RequestPart
                                                                   @Parameter(description = "식물 이미지 파일")
                                                                   MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(plantService.uploadPlantImage(image)));
    }
}