package com.sparta.ssaktium.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank
    @Email
    @Schema(description = "이메일", example = "email@gmail.com")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
            message = "비밀번호는 최소 8자리 이상, 숫자, 영문, 특수문자를 1자 이상 포함되어야 합니다.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "비밀번호", example = "111111N@")
    private String password;

    @NotBlank
    @Schema(description = "유저 이름", example = "손흥민")
    private String userName;

    @NotNull
    @Schema(description = "출생년도", example = "1997")
    private String birthYear;
}
