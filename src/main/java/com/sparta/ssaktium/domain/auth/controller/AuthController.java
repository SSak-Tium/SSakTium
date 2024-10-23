package com.sparta.ssaktium.domain.auth.controller;

import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SigninResponseDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.service.AuthService;
import jakarta.security.auth.message.AuthException;
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

    // 회원가입
    @PostMapping("/v1/auth/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(authService.signup(signupRequestDto)));
    }

    // 로그인
    @PostMapping("/v1/auth/signin")
    public ResponseEntity<ApiResponse<SigninResponseDto>> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) throws AuthException {
        return ResponseEntity.ok(ApiResponse.success(authService.signin(signinRequestDto)));
    }
}