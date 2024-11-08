package com.sparta.ssaktium.domain.auth.email.controller;

import com.amazonaws.Response;
import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.auth.email.dto.EmailCertificationRequestDto;
import com.sparta.ssaktium.domain.auth.email.dto.VerifyCertificationNumberRequestDto;
import com.sparta.ssaktium.domain.auth.email.service.EmailCertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailCertificationController {

    private final EmailCertificationService emailCertificationService;

    @PostMapping("/v2/auth/email-certification")
    public ResponseEntity<CommonResponse<String>> emailCertification(
            @RequestBody @Valid
            EmailCertificationRequestDto requestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(emailCertificationService.emailCertification(requestDto)));
    }

    @PostMapping("/v2/auth/verify-certification")
    public ResponseEntity<CommonResponse<String>> verifyCertificationNumber(
            @RequestBody @Valid
            VerifyCertificationNumberRequestDto requestDto
    ) {
        return ResponseEntity.ok(CommonResponse.success(emailCertificationService.verifyCertificationNumber(requestDto)));
    }
}
