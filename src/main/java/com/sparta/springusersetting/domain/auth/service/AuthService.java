package com.sparta.springusersetting.domain.auth.service;

import com.sparta.springusersetting.config.JwtUtil;
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

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getUserName(),
                signupRequestDto.getBirthDate(),
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        return new SignupResponseDto(bearerToken);
    }

    // 로그인
    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                NotFoundUserException::new);

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환
        if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        // UserStatus 가 DELETED 면 로그인 불가능
        if (user.getUserStatus().equals(UserStatus.DELETED)) {
            throw new DeletedUserException();
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponseDto(bearerToken);
    }
}
