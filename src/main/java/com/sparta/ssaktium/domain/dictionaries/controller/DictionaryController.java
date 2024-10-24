package com.sparta.ssaktium.domain.dictionaries.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryListResponseDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryResponseDto;
import com.sparta.ssaktium.domain.dictionaries.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    // 식물도감 등록
    @PostMapping(value = "/v1/dictionaries")
    public ResponseEntity<ApiResponse<DictionaryResponseDto>> createDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart DictionaryRequestDto dictionaryRequestDto,
            @RequestPart MultipartFile image) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(dictionaryService.createDictionary(authUser.getUserId(), dictionaryRequestDto, image)));
    }

    // 식물도감 단건 조회
    @GetMapping("/v1/dictionaries/{id}")
    public ResponseEntity<ApiResponse<DictionaryResponseDto>> getDictionary(@AuthenticationPrincipal AuthUser authUser, @PathVariable long id) {
        return ResponseEntity.ok(ApiResponse.success(dictionaryService.getDictionary(authUser.getUserId(), id)));
    }

    // 식물도감 리스트 조회
    @GetMapping("/v1/dictionaries")
    public ResponseEntity<ApiResponse<Page<DictionaryListResponseDto>>> getDictionaryList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(dictionaryService.getDictionaryList(page, size)));
    }

    // 식물도감 수정
    @PutMapping("/v1/dictionaries/{id}")
    public ResponseEntity<ApiResponse<DictionaryResponseDto>> updateDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart DictionaryUpdateRequestDto dictionaryUpdateRequestDto,
            @RequestPart MultipartFile image,
            @PathVariable long id) throws IOException {
        return ResponseEntity.ok(ApiResponse.success(dictionaryService.updateDictionary(authUser.getUserId(), dictionaryUpdateRequestDto, image, id)));
    }

    // 식물도감 삭제
    @DeleteMapping("/v1/dictionaries/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDictionary(@AuthenticationPrincipal AuthUser authUser, @PathVariable long id) {
        return ResponseEntity.ok(ApiResponse.success(dictionaryService.deleteDictionary(authUser.getUserId(), id)));
    }
}
