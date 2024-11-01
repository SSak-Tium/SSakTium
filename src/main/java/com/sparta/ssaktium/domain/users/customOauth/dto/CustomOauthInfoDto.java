package com.sparta.ssaktium.domain.users.customOauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomOauthInfoDto {
    private String socialId;
    private String userName;
    private String email;
    private String birthYear;


    public CustomOauthInfoDto(String socialId, String userName, String email) {
        this.socialId = socialId;
        this.userName = userName;
        this.email = email;
    }

    public CustomOauthInfoDto(String socialId, String userName, String email, String birthYear) {
        this.socialId = socialId;
        this.userName = userName;
        this.email = email;
        this.birthYear = birthYear;
    }

    public static CustomOauthInfoDto addGoogleId(String socialId, String userName, String email) {
        return new CustomOauthInfoDto(socialId, userName, email);
    }
}
