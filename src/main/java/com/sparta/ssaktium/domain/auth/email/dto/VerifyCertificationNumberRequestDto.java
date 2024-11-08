package com.sparta.ssaktium.domain.auth.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VerifyCertificationNumberRequestDto {

    @Email
    @NotBlank
    @Schema(description = "이메일", example = "email@gmail.com")
    private final String Email;

    @NotBlank
    @Schema(description = "인증번호", example = "1234")
    private final String InputCertificationNumber;
}
