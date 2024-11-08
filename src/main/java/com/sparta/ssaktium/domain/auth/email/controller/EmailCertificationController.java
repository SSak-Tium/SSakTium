package com.sparta.ssaktium.domain.auth.email.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.auth.email.dto.EmailCertificationRequestDto;
import com.sparta.ssaktium.domain.auth.email.dto.VerifyCertificationNumberRequestDto;
import com.sparta.ssaktium.domain.auth.email.service.EmailCertificationService;
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
@Tag(name = "이메일 인증 기능", description = "인증번호 이메일 전송과 인증번호 검사를 할 수 있는 기능")
public class EmailCertificationController {

    private final EmailCertificationService emailCertificationService;

    @PostMapping("/v2/auth/email-certification")
    @Operation(summary = "인증번호 전송", description = "이메일로 인증번호 전송하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<String>> emailCertification(@Valid
                                                                     @RequestBody
                                                                     @Parameter(description = "이메일 입력")
                                                                     EmailCertificationRequestDto requestDto) {
        return ResponseEntity.ok(CommonResponse.success(emailCertificationService.emailCertification(requestDto)));
    }

    @PostMapping("/v2/auth/verify-certification")
    @Operation(summary = "인증번호 확인", description = "받은 인증번호를 확인하는 API")
    @ApiResponse(responseCode = "200", description = "요청이 성공적으로 처리되었습니다.")
    public ResponseEntity<CommonResponse<String>> verifyCertificationNumber(@Valid
                                                                            @RequestBody
                                                                            @Parameter(description = "인증번호 확인 정보 입력")
                                                                            VerifyCertificationNumberRequestDto requestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(emailCertificationService.verifyCertificationNumber(requestDto)));
    }
}
