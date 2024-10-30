package com.sparta.ssaktium.domain.auth.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SigninResponseDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     *
     * @param signupRequestDto 사용자 등록 회원정보 : Email, Password, 이름, 출생년도
     * @return
     */
    @PostMapping("/v1/auth/signup")
    public ResponseEntity<CommonResponse<SignupResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(authService.signup(signupRequestDto)));
    }

    /**
     * 로그인
     *
     * @param signinRequestDto 로그인 : Email, Password
     * @return
     */
    @PostMapping("/v1/auth/signin")
    public ResponseEntity<CommonResponse<SigninResponseDto>> signin(
            @Valid @RequestBody SigninRequestDto signinRequestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(authService.signin(signinRequestDto)));
    }
}