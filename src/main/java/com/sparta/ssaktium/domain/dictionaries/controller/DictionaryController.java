package com.sparta.ssaktium.domain.dictionaries.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryImageResponseDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryListResponseDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryResponseDto;
import com.sparta.ssaktium.domain.dictionaries.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "식물도감 관리기능", description = "식물도감을 등록하고 관리할 수 있는 기능")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/v1/dictionaries")
    @Operation(summary = "식물도감 등록", description = "식물도감 등록하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<DictionaryResponseDto>> createDictionary(@AuthenticationPrincipal
                                                                                  AuthUser authUser,
                                                                                  @RequestPart
                                                                                  @Parameter(description = "식물도감 정보")
                                                                                  DictionaryRequestDto dictionaryRequestDto,
                                                                                  @RequestPart
                                                                                  @Parameter(description = "식물도감 프로필 사진")
                                                                                  MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.createDictionary(authUser.getUserId(), dictionaryRequestDto, image)));
    }

    @GetMapping("/v1/dictionaries/{id}")
    @Operation(summary = "식물도감 단건 조회", description = "식물도감 단건 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<DictionaryResponseDto>> getDictionary(@AuthenticationPrincipal
                                                                               AuthUser authUser,
                                                                               @PathVariable
                                                                               @Parameter(description = "요청할 식물도감 Id", example = "1")
                                                                               long id) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.getDictionary(authUser.getUserId(), id)));
    }

    @GetMapping("/v1/dictionaries")
    @Operation(summary = "식물도감 리스트 조회", description = "식물도감 리스트 조회하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<Page<DictionaryListResponseDto>>> getDictionaryList(@AuthenticationPrincipal
                                                                                             AuthUser authUser,
                                                                                             @RequestParam(defaultValue = "1")
                                                                                             @Parameter(description = "page", example = "1")
                                                                                             int page,
                                                                                             @RequestParam(defaultValue = "10")
                                                                                             @Parameter(description = "size", example = "10")
                                                                                             int size) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.getDictionaryList(authUser.getUserId(), page, size)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/v1/dictionaries/{id}")
    @Operation(summary = "식물도감 수정", description = "식물도감 수정하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<DictionaryResponseDto>> updateDictionary(@AuthenticationPrincipal
                                                                                  AuthUser authUser,
                                                                                  @RequestBody
                                                                                  @Parameter(description = "식물도감 정보 수정")
                                                                                  DictionaryUpdateRequestDto dictionaryUpdateRequestDto,
                                                                                  @PathVariable
                                                                                  @Parameter(description = "요청할 식물도감 Id", example = "1")
                                                                                  long id) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.updateDictionary(authUser.getUserId(), dictionaryUpdateRequestDto, id)));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/v1/dictionaries/{id}/update-image")
    @Operation(summary = "식물도감 이미지 수정", description = "식물도감의 프로필 이미지를 수정하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<DictionaryImageResponseDto>> updateDictionary(@AuthenticationPrincipal
                                                                                       AuthUser authUser,
                                                                                       @PathVariable
                                                                                       @Parameter(description = "요청할 식물도감 Id", example = "1")
                                                                                       long id,
                                                                                       @RequestPart
                                                                                       @Parameter(description = "식물도감 프로필 사진")
                                                                                       MultipartFile image) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.updateDictionaryImage(authUser.getUserId(), id, image)));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/v1/dictionaries/{id}")
    @Operation(summary = "식물도감 삭제", description = "식물도감을 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<String>> deleteDictionary(@AuthenticationPrincipal
                                                                   AuthUser authUser,
                                                                   @PathVariable
                                                                   @Parameter(description = "요청할 식물도감 Id", example = "1")
                                                                   long id) {
        return ResponseEntity.ok(CommonResponse.success(dictionaryService.deleteDictionary(authUser.getUserId(), id)));
    }
}
