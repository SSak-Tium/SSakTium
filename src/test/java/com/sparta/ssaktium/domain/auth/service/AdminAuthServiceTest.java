package com.sparta.ssaktium.domain.auth.service;

import com.sparta.ssaktium.config.JwtUtil;
import com.sparta.ssaktium.domain.auth.dto.request.AdminSignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.auth.dto.response.SignupResponseDto;
import com.sparta.ssaktium.domain.auth.exception.DuplicateEmailException;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AdminAuthServiceTest {

    @InjectMocks
    private AdminAuthService adminAuthService;

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
    private User adminUser;
    private String encodedPassword;
    private AdminSignupRequestDto adminSignupRequestDto;
    private String token;


    @BeforeEach
    void setUp(){
        userId = 1L;
        authUser = mock(AuthUser.class);
        ReflectionTestUtils.setField(authUser, "userId", 1L);
        user = new User("email@gmail.com", "password", "name","0000", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        adminUser = new User("email@gmail.com", "password", "name","0000", UserRole.ROLE_ADMIN);
        ReflectionTestUtils.setField(adminUser, "id", 2L);
        encodedPassword = "encodedPassword";
        token = "Bearer Token";

        adminSignupRequestDto = new AdminSignupRequestDto("email", "password", "name");
        savedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER);
        deletedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(deletedUser, "deleted", true);
    }

    @Test
    void 운영자_회원가입_성공() {
        // given
        given(userRepository.existsByEmail(adminSignupRequestDto.getEmail())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);
        given(userRepository.save(any(User.class))).willReturn(adminUser);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn(token);

        // when
        SignupResponseDto responseDto = adminAuthService.adminSignup(adminSignupRequestDto);

        //then
        assertThat(responseDto.getBearerToken()).isEqualTo(token);
    }

    @Test
    void 운영자_회원가입_실패_이메일_중복() {
        // given
        given(userRepository.existsByEmail(adminSignupRequestDto.getEmail())).willReturn(true);

        // when
        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> adminAuthService.adminSignup(adminSignupRequestDto)
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("409 DUPLICATE_EMAIL 중복된 이메일입니다.");
    }
}