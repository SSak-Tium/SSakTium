package com.sparta.ssaktium.domain.dictionaries.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.dictionaries.service.FavoriteDictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "식물도감 관심 등록 및 해제 기능", description = "식물도감을 관심 등록하고 해제할 수 있는 기능")
public class FavoriteDictionaryController {
    private final FavoriteDictionaryService favoriteDictionaryService;

    @PutMapping(value = "/v1/favorites/dictionaries/{id}")
    @Operation(summary = "식물도감 관심 등록 및 해제", description = "식물도감 관심 등록 및 해제하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<String>> updateDictionary(@AuthenticationPrincipal
                                                                   AuthUser authUser,
                                                                   @PathVariable
                                                                   @Parameter(description = "요청할 식물도감 Id")
                                                                   long id
    ) {
        return ResponseEntity.ok(CommonResponse.success(favoriteDictionaryService.toggleFavoriteDictionary(authUser.getUserId(), id)));
    }
}
