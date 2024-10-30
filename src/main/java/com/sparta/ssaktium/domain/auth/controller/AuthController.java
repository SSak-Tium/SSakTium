package com.sparta.ssaktium.domain.auth.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SigninResponseDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.service.AuthService;
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
@Tag(name = "회원가입/로그인 관리기능", description = "회원가입과 로그인할 수 있는 기능")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/auth/signup")
    @Operation(summary = "회원가입", description = "회원가입하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<SignupResponseDto>> signup(@Valid
                                                                    @RequestBody
                                                                    @Parameter(description = "회원정보 입력")
                                                                    SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(authService.signup(signupRequestDto)));
    }

    @PostMapping("/v1/auth/signin")
    @Operation(summary = "로그인", description = "로그인하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<SigninResponseDto>> signin(@Valid
                                                                    @RequestBody
                                                                    @Parameter(description = "로그인정보 입력")
                                                                    SigninRequestDto signinRequestDto) {
        return ResponseEntity.ok(CommonResponse.success(authService.signin(signinRequestDto)));
    }
}