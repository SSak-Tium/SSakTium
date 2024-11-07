package com.sparta.ssaktium.domain.auth.email.service;

import com.sparta.ssaktium.config.EmailConfig;
import com.sparta.ssaktium.domain.auth.email.dto.EmailCertificationRequestDto;
import com.sparta.ssaktium.domain.auth.exception.BusinessLogicException;
import com.sparta.ssaktium.domain.auth.exception.DuplicateEmailException;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailCertificationService {

    private final EmailConfig emailConfig;

    private final UserService userService;

    public String emailCertification(EmailCertificationRequestDto requestDto) {
        // 입력한 이메일로 중복 유무 확인
        String email = requestDto.getEmail();
        boolean isExisted = userService.existsByEmail(email);
        if(isExisted) throw new DuplicateEmailException();

        // 4자리 인증번호 생성
        String certificationNumber = getCertificationNumber();

        // 이메일 발신 성공 여부 확인
        boolean isSuccessed = emailConfig.sendCertificationMain(email, certificationNumber);

        // 실패시 예외처리
        if (!isSuccessed) throw new BusinessLogicException();


        return "이메일이 정상적으로 전송되었습니다.";
    }

    // 4자리 인증번호 생성 메서드
    public static String getCertificationNumber() {
        String certificationNumber = "";
        for (int count = 0; count < 4; count++) {
            certificationNumber += (int) (Math.random() * 10);
        }

        return certificationNumber;
    }
}
