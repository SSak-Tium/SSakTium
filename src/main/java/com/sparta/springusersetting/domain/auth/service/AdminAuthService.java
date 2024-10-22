package com.sparta.springusersetting.domain.auth.service;

import com.sparta.springusersetting.config.JwtUtil;
import com.sparta.springusersetting.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.springusersetting.domain.auth.dto.request.SigninRequestDto;
import com.sparta.springusersetting.domain.auth.dto.request.SignupRequestDto;
import com.sparta.springusersetting.domain.auth.dto.response.SigninResponseDto;
import com.sparta.springusersetting.domain.auth.dto.response.SignupResponseDto;
import com.sparta.springusersetting.domain.auth.exception.DeletedUserException;
import com.sparta.springusersetting.domain.auth.exception.DuplicateEmailException;
import com.sparta.springusersetting.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.springusersetting.domain.users.entity.User;
import com.sparta.springusersetting.domain.users.enums.UserRole;
import com.sparta.springusersetting.domain.users.enums.UserStatus;
import com.sparta.springusersetting.domain.users.exception.NotFoundUserException;
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
        User newUser = User.addAdminUser(adminSignupRequestDto, encodedPassword, userRole);

        // DB 저장
        User savedUser = userRepository.save(newUser);

        // 토큰 생성
        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new SignupResponseDto(bearerToken);
    }
}
