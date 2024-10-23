package com.sparta.ssaktium.domain.auth.service;

import com.sparta.ssaktium.config.JwtUtil;
import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SigninResponseDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.exception.DeletedUserException;
import com.sparta.ssaktium.domain.auth.exception.DuplicateEmailException;
import com.sparta.ssaktium.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.ssaktium.domain.users.entity.Users;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.enums.UserStatus;
import com.sparta.ssaktium.domain.users.exception.NotFoundUserException;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        UserRole userRole = UserRole.of(UserRole.ROLE_USER.getUserRole());

        Users newUsers = new Users(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getUserName(),
                signupRequestDto.getBirthDate(),
                userRole
        );
        Users savedUsers = userRepository.save(newUsers);

        String bearerToken = jwtUtil.createToken(savedUsers.getId(), savedUsers.getEmail(), userRole);

        return new SignupResponseDto(bearerToken);
    }

    // 로그인
    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {
        Users users = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                NotFoundUserException::new);

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환
        if (!passwordEncoder.matches(signinRequestDto.getPassword(), users.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        // UserStatus 가 DELETED 면 로그인 불가능
        if (users.getUserStatus().equals(UserStatus.DELETED)) {
            throw new DeletedUserException();
        }

        String bearerToken = jwtUtil.createToken(users.getId(), users.getEmail(), users.getUserRole());

        return new SigninResponseDto(bearerToken);
    }
}
