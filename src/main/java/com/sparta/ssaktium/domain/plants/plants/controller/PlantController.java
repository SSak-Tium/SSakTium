package com.sparta.ssaktium.domain.plants.plants.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.plants.dto.requestDto.PlantRequestDto;
import com.sparta.ssaktium.domain.plants.plants.dto.requestDto.PlantUpdateRequestDto;
import com.sparta.ssaktium.domain.plants.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.plants.service.PlantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PlantController {

    private final PlantService plantService;

    @PostMapping("/plants")
    public ResponseEntity<CommonResponse<PlantResponseDto>> createPlant(@AuthenticationPrincipal AuthUser authUser,
                                                                        @RequestPart PlantRequestDto requestDto,
                                                                        @RequestPart(required = false) MultipartFile image) {
        PlantResponseDto responseDto = plantService.createPlant(authUser.getUserId(), requestDto, image);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * 내 식물 단건조회
     *
     * @param authUser
     * @param id
     * @return
     */
    @GetMapping("/plants/{id}")
    public ResponseEntity<CommonResponse<PlantResponseDto>> getPlant(@AuthenticationPrincipal AuthUser authUser,
                                                                     @PathVariable Long id) {

        PlantResponseDto responseDto = plantService.getPlant(authUser.getUserId(), id);
        return ResponseEntity.ok(CommonResponse.success(responseDto));

    }

    /**
     * 내 식물 다건 조회
     *
     * @param authUser
     * @return
     */
    @GetMapping("/plants")
    public ResponseEntity<CommonResponse<List<PlantResponseDto>>> getAllPlants(@AuthenticationPrincipal AuthUser authUser) {
        List<PlantResponseDto> responseDto = plantService.getAllPlants(authUser.getUserId());
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * @param authUser
     * @param id
     * @param requestDto
     * @return
     */
    @PutMapping("/plants/{id}")
    public ResponseEntity<CommonResponse<PlantResponseDto>> updatePlant(@AuthenticationPrincipal AuthUser authUser,
                                                                        @PathVariable Long id,
                                                                        @RequestPart PlantUpdateRequestDto requestDto) {
        PlantResponseDto responseDto = plantService.updatePlant(authUser.getUserId(), id, requestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }

    /**
     * 내 식물 삭제
     *
     * @param authUser
     * @param id
     * @return
     */
    @DeleteMapping("/plants/{id}")
    public ResponseEntity<CommonResponse<String>> deletePlant(@AuthenticationPrincipal AuthUser authUser,
                                                              @PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.success(plantService.deletePlant(authUser.getUserId(), id)));
    }

    /**
     * 수정 시 이미지 등록 API
     *
     * @param authUser
     * @param image
     * @return
     */
    @PostMapping("/plants/image")
    public ResponseEntity<CommonResponse<String>> uploadPlantImage(@AuthenticationPrincipal AuthUser authUser,
                                                                   @RequestPart MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(plantService.uploadPlantImage(authUser.getUserId(), image)));
    }
}