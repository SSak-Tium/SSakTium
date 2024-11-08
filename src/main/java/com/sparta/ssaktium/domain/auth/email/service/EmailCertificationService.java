package com.sparta.ssaktium.domain.auth.email.service;

import com.sparta.ssaktium.config.EmailConfig;
import com.sparta.ssaktium.domain.auth.email.dto.EmailCertificationRequestDto;
import com.sparta.ssaktium.domain.auth.email.dto.VerifyCertificationNumberRequestDto;
import com.sparta.ssaktium.domain.auth.exception.BusinessLogicException;
import com.sparta.ssaktium.domain.auth.exception.DuplicateEmailException;
import com.sparta.ssaktium.domain.auth.exception.InvalidVerificationCodeException;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCertificationService {

    private final EmailConfig emailConfig;

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;

    public String emailCertification(EmailCertificationRequestDto requestDto) {
        // 입력한 이메일로 중복 유무 확인
        String email = requestDto.getEmail();
        boolean isExisted = userService.existsByEmail(email);
        if (isExisted) throw new DuplicateEmailException();

        // 4자리 인증번호 생성
        String certificationNumber = getCertificationNumber();

        // Redis 에 이메일과 인증번호를 저장 (5분 유효기간 설정)
        redisTemplate.opsForValue().set(email + ":code", certificationNumber, 10, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(email + ":verified", false, 10, TimeUnit.MINUTES);

        // 이메일 발신 작업을 비동기적으로 처리
        CompletableFuture<Boolean> isSuccessed = emailConfig.sendCertificationEmail(email, certificationNumber);

        // 비동기 결과를 처리하는 후속 작업 추가
        isSuccessed.thenAcceptAsync(success -> {
            if (!success) {
                // 이메일 발송 실패 시 예외 처리
                throw new BusinessLogicException();
            }

            // 이메일 발송 성공 시 로그 기록
            log.info("이메일 인증번호 전송 완료: {}", certificationNumber);
        }).exceptionally(ex -> {
            // 예외 처리: 이메일 발송 중 문제가 발생하면 로깅
            log.error("이메일 발송 중 오류 발생: {}", email, ex);
            return null;
        });

        return "이메일이 정상적으로 전송되었습니다.";
    }

    // 인증번호 확인
    public String verifyCertificationNumber(VerifyCertificationNumberRequestDto requestDto) {
        String email = requestDto.getEmail();
        String storedCertificationNumber = (String) redisTemplate.opsForValue().get(email + ":code");

        if (storedCertificationNumber == null || !storedCertificationNumber.equals(requestDto.getInputCertificationNumber())) {
            throw new InvalidVerificationCodeException();
        }

        // 인증 성공 시 인증 상태를 true 로 변경
        redisTemplate.opsForValue().set(email + ":verified", true, 5, TimeUnit.MINUTES);
        return "인증이 성공적으로 완료되었습니다.";
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
