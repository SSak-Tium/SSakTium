package com.sparta.springusersetting.domain.auth.service;

import com.sparta.springusersetting.config.JwtUtil;
import com.sparta.springusersetting.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.springusersetting.domain.auth.dto.response.SignupResponseDto;
import com.sparta.springusersetting.domain.auth.exception.DuplicateEmailException;
import com.sparta.springusersetting.domain.users.entity.Users;
import com.sparta.springusersetting.domain.users.enums.UserRole;
import com.sparta.springusersetting.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 관리자 회원가입
    public SignupResponseDto adminSignup(AdminSignupRequestDto adminSignupRequestDto) {

        if (userRepository.existsByEmail(adminSignupRequestDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        // Password Encoding
        String encodedPassword = passwordEncoder.encode(adminSignupRequestDto.getPassword());

        // UserRole 관리자 설정
        UserRole userRole = UserRole.of(UserRole.ROLE_ADMIN.getUserRole());

        // User Entity 관리자 추가
        Users newUsers = Users.addAdminUser(adminSignupRequestDto, encodedPassword, userRole);

        // DB 저장
        Users savedUsers = userRepository.save(newUsers);

        // 토큰 생성
        String bearerToken = jwtUtil.createToken(savedUsers.getId(), savedUsers.getEmail(), userRole);

        return new SignupResponseDto(bearerToken);
    }
}
