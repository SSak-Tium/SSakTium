package com.sparta.ssaktium.domain.users.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String nickname;
    private String email;
    private String birthYear;

    public KakaoUserInfoDto(Long id, String nickname, String email, String birthYear) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.birthYear = birthYear;
    }
}