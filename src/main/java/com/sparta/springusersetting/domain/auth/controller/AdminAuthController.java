package com.sparta.springusersetting.domain.auth.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.springusersetting.domain.auth.dto.response.SignupResponseDto;
import com.sparta.springusersetting.domain.auth.service.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    // 관리자 회원가입
    @PostMapping("/v1/auth/admin/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> adminSignup(@Valid @RequestBody AdminSignupRequestDto adminSignupRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(adminAuthService.adminSignup(adminSignupRequestDto)));
    }
}