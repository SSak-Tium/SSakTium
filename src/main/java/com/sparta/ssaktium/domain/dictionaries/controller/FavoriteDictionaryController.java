package com.sparta.ssaktium.domain.dictionaries.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryImageResponseDto;
import com.sparta.ssaktium.domain.dictionaries.service.DictionaryService;
import com.sparta.ssaktium.domain.dictionaries.service.FavoriteDictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteDictionaryController {
    private final FavoriteDictionaryService favoriteDictionaryService;

    // 식물도감 관심 등록
    @PostMapping(value = "/v1/dictionaries/{id}")
    public ResponseEntity<ApiResponse<String>> updateDictionary(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long id
    ) {
        return ResponseEntity.ok(ApiResponse.success(favoriteDictionaryService.pushFavoriteDictionary(authUser.getUserId(), id)));
    }
}
