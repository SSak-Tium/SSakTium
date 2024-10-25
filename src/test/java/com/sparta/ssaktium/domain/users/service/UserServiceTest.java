package com.sparta.ssaktium.domain.users.service;

import com.sparta.ssaktium.domain.auth.dto.request.SigninRequestDto;
import com.sparta.ssaktium.domain.auth.dto.request.SignupRequestDto;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private long userId;
    private User savedUser;
    private String encodedPassword;


    @BeforeEach
    void setUp(){
        userId = 1L;
        user = new User("email@gmail.com", "password", "name","0000", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", userId);
        encodedPassword = "encodedPassword";
        savedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER);
    }

    @Test
    void 사용자_조회_성공() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponseDto responseDto = userService.getUser(userId);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void changePassword() {
    }

    @Test
    void deleteUser() {
    }
}