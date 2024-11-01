package com.sparta.ssaktium.domain.users.customOauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomOauthInfoDto {
    private Long id;
    private String userName;
    private String email;
    private String birthYear;

    public CustomOauthInfoDto(Long id, String userName, String email, String birthYear) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.birthYear = birthYear;
    }

    public CustomOauthInfoDto(Long id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public CustomOauthInfoDto( String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public CustomOauthInfoDto( String userName, String email, String birthYear) {
        this.userName = userName;
        this.email = email;
        this.birthYear = birthYear;
    }
}
