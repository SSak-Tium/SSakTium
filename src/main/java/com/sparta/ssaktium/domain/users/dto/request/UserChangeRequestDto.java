package com.sparta.ssaktium.domain.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserChangeRequestDto {

    @Schema(description = "프로필 이미지 URL", example = "image.com")
    private final String profileImageUrl;

    @Schema(description = "유저 이름", example = "손흥민")
    private final String userName;
}
