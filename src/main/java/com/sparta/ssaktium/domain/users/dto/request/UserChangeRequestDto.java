package com.sparta.ssaktium.domain.users.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserChangeRequestDto {
    private final String profileImageUrl;
    private final String userName;
}
