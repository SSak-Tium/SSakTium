package com.sparta.ssaktium.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequestDto {

    @NotBlank
    @Email
    @Schema(description = "이메일", example = "email@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "111111N@")
    private String password;
}
