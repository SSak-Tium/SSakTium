package com.sparta.ssaktium.domain.auth.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "운영자 회원가입 관리기능", description = "운영자가 회원가입할 수 있는 기능")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/v1/auth/admin/signup")
    @Operation(summary = "운영자 회원가입", description = "운영자의 회원가입 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<SignupResponseDto>> adminSignup(@Valid
                                                                         @RequestBody
                                                                         @Parameter(description = "운영자 회원정보 입력")
                                                                         AdminSignupRequestDto adminSignupRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(adminAuthService.adminSignup(adminSignupRequestDto)));
    }
}