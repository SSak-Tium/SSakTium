package com.sparta.ssaktium.domain.auth.email.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailCertificationRequestDto {

    @Email
    @NotBlank
    @Schema(description = "이메일", example = "email@gmail.com")
    private String email;
}
