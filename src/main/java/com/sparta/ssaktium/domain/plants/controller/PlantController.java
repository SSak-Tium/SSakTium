package com.sparta.ssaktium.domain.plants.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.dto.requestDto.PlantRequestDto;
import com.sparta.ssaktium.domain.plants.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    /**
     * 내 식물 등록
     * @param authUser
     * @param requestDto
     * @param image
     * @return
     * @throws IOException
     */
    @PostMapping("/v1/plants")
    public ResponseEntity<ApiResponse<PlantResponseDto>> createPlant (@AuthenticationPrincipal AuthUser authUser,
                                                                      @RequestPart PlantRequestDto requestDto,
                                                                      @RequestPart(required = false) MultipartFile image) throws IOException {
        PlantResponseDto responseDto = plantService.createPlant(authUser.getUserId(), requestDto, image);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * 내 식물 단건조회
     * @param authUser
     * @param id
     * @return
     */
    @GetMapping("/v1/plants/{id}")
    public ResponseEntity<ApiResponse<PlantResponseDto>> getPlant(@AuthenticationPrincipal AuthUser authUser,
                                                                  @PathVariable Long id) {

        PlantResponseDto responseDto = plantService.getPlant(authUser.getUserId(), id);
        return ResponseEntity.ok(ApiResponse.success(responseDto));

    }

    /**
     * 내 식물 다건 조회
     * @param authUser
     * @return
     */
    @GetMapping("/v1/plants")
    public ResponseEntity<ApiResponse<List<PlantResponseDto>>> getAllPlants(@AuthenticationPrincipal AuthUser authUser) {
        List<PlantResponseDto> responseDto = plantService.getAllPlants(authUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @PutMapping("/v1/plants/{id}")
    public ResponseEntity<ApiResponse<PlantResponseDto>> updatePlant(@AuthenticationPrincipal AuthUser authUser,
                                                                     @PathVariable Long id,
                                                                     @RequestPart PlantRequestDto requestDto,
                                                                     @RequestPart(required = false) MultipartFile image) throws IOException {
        PlantResponseDto responseDto = plantService.updatePlant(authUser.getUserId(), id, requestDto, image);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    /**
     * 내 식물 삭제
     * @param authUser
     * @param id
     * @return
     */
    @DeleteMapping("/v1/plants/{id}")
    public ResponseEntity<ApiResponse<String>> deletePlant(@AuthenticationPrincipal AuthUser authUser,
                                                         @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(plantService.deltePlant(authUser.getUserId(),id)));
    }
}
