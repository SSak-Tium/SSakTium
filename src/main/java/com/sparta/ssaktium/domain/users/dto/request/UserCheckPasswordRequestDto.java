package com.sparta.ssaktium.domain.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCheckPasswordRequestDto {

    @Schema(description = "비밀번호", example = "111111N@")
    private String password;
}
