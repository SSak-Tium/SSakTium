package com.sparta.ssaktium.domain.dictionaries.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryImageResponseDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryListResponseDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryResponseDto;
import com.sparta.ssaktium.domain.dictionaries.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    /**
     * 식물도감 등록
     *
     * @param authUser             로그인 유저
     * @param dictionaryRequestDto 제목, 내용
     * @param image                프로필 이미지
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/v1/dictionaries")
    public ResponseEntity<CommonResponse<DictionaryResponseDto>> createDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestPart DictionaryRequestDto dictionaryRequestDto,
            @RequestPart MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.createDictionary(authUser.getUserId(), dictionaryRequestDto, image)));
    }

    /**
     * 식물도감 단건 조회
     *
     * @param authUser 로그인 유저
     * @param id       식물도감 id
     * @return
     */
    @GetMapping("/v1/dictionaries/{id}")
    public ResponseEntity<CommonResponse<DictionaryResponseDto>> getDictionary(@AuthenticationPrincipal AuthUser authUser, @PathVariable long id) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.getDictionary(authUser.getUserId(), id)));
    }

    /**
     * 식물도감 리스트 조회
     *
     * @param page 페이지
     * @param size 출력 수
     * @return
     */
    @GetMapping("/v1/dictionaries")
    public ResponseEntity<CommonResponse<Page<DictionaryListResponseDto>>> getDictionaryList(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.getDictionaryList(authUser.getUserId(), page, size)));
    }

    /**
     * 식물도감 수정
     *
     * @param authUser                   로그인 유저
     * @param dictionaryUpdateRequestDto 제목, 내용
     * @param id                         식물도감 id
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/v1/dictionaries/{id}")
    public ResponseEntity<CommonResponse<DictionaryResponseDto>> updateDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody DictionaryUpdateRequestDto dictionaryUpdateRequestDto,
            @PathVariable long id
    ) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.updateDictionary(authUser.getUserId(), dictionaryUpdateRequestDto, id)));
    }

    /**
     * 식물도감 이미지 수정
     *
     * @param authUser 로그인 유저
     * @param id       식물도감 id
     * @param image    프로필 이미지
     * @return
     */
    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/v1/dictionaries/{id}/image")
    public ResponseEntity<CommonResponse<DictionaryImageResponseDto>> updateDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id,
            @RequestPart MultipartFile image
    ) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.updateDictionaryImage(authUser.getUserId(), id, image)));
    }

    /**
     * 식물도감 삭제
     *
     * @param authUser 로그인 유저
     * @param id       식물도감 id
     * @return
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/v1/dictionaries/{id}")
    public ResponseEntity<CommonResponse<String>> deleteDictionary(@AuthenticationPrincipal AuthUser authUser, @PathVariable long id) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.deleteDictionary(authUser.getUserId(), id)));
    }
}
