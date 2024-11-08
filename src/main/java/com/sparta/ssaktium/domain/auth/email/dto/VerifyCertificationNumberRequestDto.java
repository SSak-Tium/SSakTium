package com.sparta.ssaktium.domain.auth.email.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VerifyCertificationNumberRequestDto {

    @Email
    @NotBlank
    private final String Email;

    @NotBlank
    private final String InputCertificationNumber;
}
