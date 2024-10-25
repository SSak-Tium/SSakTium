package com.sparta.ssaktium.domain.auth.service;

import com.sparta.ssaktium.config.JwtUtil;
import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SigninResponseDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.exception.DeletedUserException;
import com.sparta.ssaktium.domain.auth.exception.DuplicateEmailException;
import com.sparta.ssaktium.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.enums.UserStatus;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    private AuthUser authUser;
    private User user;
    private long userId;
    private User savedUser;
    private User deletedUser;
    private String encodedPassword;
    private SignupRequestDto signupRequestDto;
    private SigninRequestDto signinRequestDto;
    private String token;


    @BeforeEach
    void setUp(){
        userId = 1L;
        authUser = mock(AuthUser.class);
        ReflectionTestUtils.setField(authUser, "userId", 1L);
        user = new User("email@gmail.com", "password", "name","0000", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        encodedPassword = "encodedPassword";
        token = "Bearer Token";

        signupRequestDto = new SignupRequestDto("email", "password", "name", "1997");
        signinRequestDto = new SigninRequestDto("email", "password");
        savedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER);
        deletedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(deletedUser, "userStatus", UserStatus.DELETED);
    }

    @Test
    void 사용자_회원가입_성공() {
        // given
        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(false);
        given(passwordEncoder.encode(signupRequestDto.getPassword())).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        given(jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), UserRole.ROLE_USER)).willReturn(token);

        // when
        SignupResponseDto responseDto = authService.signup(signupRequestDto);

        //then
        assertThat(responseDto.getBearerToken()).isEqualTo(token);
    }

    @Test
    void 사용자_회원가입_실패_이메일_중복() {
        // given
        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(true);

        // when
        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> authService.signup(signupRequestDto)
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("409 CONFLICT DUPLICATE_EMAIL 중복된 이메일입니다.");
    }

    @Test
    void 로그인_성공() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), UserRole.ROLE_USER)).willReturn(token);

        // when
        SigninResponseDto responseDto = authService.signin(signinRequestDto);

        // then
        assertThat(responseDto.getBearerToken()).isEqualTo(token);
    }

    @Test
    void 로그인_실패_비밀번호_불일치() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when
        UnauthorizedPasswordException exception = assertThrows(
                UnauthorizedPasswordException.class,
                () -> authService.signin(signinRequestDto)
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("401 UNAUTHORIZED UNAUTHORIZED_PASSWORD 비밀번호를 확인해주세요.");
    }

    @Test
    void 로그인_실패_삭제된_유저() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(deletedUser));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // when
        DeletedUserException exception = assertThrows(
                DeletedUserException.class,
                () -> authService.signin(signinRequestDto)
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND DELETED_USER 탈퇴된 회원입니다.");
    }
}