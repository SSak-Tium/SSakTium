package com.sparta.ssaktium.domain.users.service;

import com.sparta.ssaktium.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.dictionaries.repository.FavoriteDictionaryRepository;
import com.sparta.ssaktium.domain.users.dto.request.UserChangePasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserChangeRequestDto;
import com.sparta.ssaktium.domain.users.dto.request.UserCheckPasswordRequestDto;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.exception.DuplicatePasswordException;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private FavoriteDictionaryRepository favoriteDictionaryRepository;

    private User user;
    private long userId;
    private User savedUser;
    private String encodedPassword;
    private List<Long> favoriteDictionaries;
    private String oldPassword;
    private String newPassword;


    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User("email@gmail.com", "password", "name", "0000", UserRole.ROLE_USER, "socialId");
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "createdAt", LocalDateTime.now());
        encodedPassword = "encodedPassword";
        savedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER, "socialId");
        favoriteDictionaries = List.of(1L, 2L);
        oldPassword = "oldPassword";
        newPassword = "newPassword";
    }

    @Test
    void 유저_조회_성공() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(favoriteDictionaryRepository.findFavoriteDictionaryIdsByUserId(anyLong())).willReturn(favoriteDictionaries);

        // when
        UserResponseDto responseDto = userService.getUser(userId);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void 유저_비밀번호_변경_성공() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(oldPassword, user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(newPassword, user.getPassword())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedNewPassword");

        // when
        String result = userService.changePassword(userId,
                new UserChangePasswordRequestDto(oldPassword, newPassword));

        // then
        assertThat(result).isEqualTo("비밀번호가 정상적으로 변경되었습니다.");
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }

    @Test
    void 유저_비밀번호_변경_실패_이전_비밀번호_불일치() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches("oldPassword", user.getPassword())).willReturn(false);

        // when
        UnauthorizedPasswordException exception = assertThrows(
                UnauthorizedPasswordException.class,
                () -> userService.changePassword(userId, new UserChangePasswordRequestDto(oldPassword, newPassword))
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("401 UNAUTHORIZED_PASSWORD 비밀번호를 확인해주세요.");
    }

    @Test
    void 유저_비밀번호_변경_실패_새_비밀번호_중복() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches("oldPassword", user.getPassword())).willReturn(true);
        given(passwordEncoder.matches(newPassword, user.getPassword())).willReturn(true);

        // when
        DuplicatePasswordException exception = assertThrows(
                DuplicatePasswordException.class,
                () -> userService.changePassword(userId, new UserChangePasswordRequestDto(oldPassword, newPassword))
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("400 DUPLICATE_PASSWORD 새 비밀번호는 이전에 사용한 비밀번호와 같을 수 없습니다.");
    }


    @Test
    void 유저_회원정보_수정_성공() {
        // given
        UserChangeRequestDto requestDto = new UserChangeRequestDto("newImageUrl", "newUserName");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(favoriteDictionaryRepository.findFavoriteDictionaryIdsByUserId(anyLong()))
                .willReturn(favoriteDictionaries);

        // when
        UserResponseDto responseDto = userService.updateUser(userId, userId, requestDto);

        // then
        assertThat(responseDto.getUserName()).isEqualTo("newUserName");
    }

    @Test
    void 유저_회원탈퇴_성공() {
        // given
        UserCheckPasswordRequestDto requestDto = new UserCheckPasswordRequestDto("correctPassword");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches("correctPassword", user.getPassword())).willReturn(true);

        // when
        String result = userService.deleteUser(userId, userId, requestDto);

        // then
        assertThat(result).isEqualTo("회원탈퇴가 정상적으로 완료되었습니다.");
        verify(userRepository).delete(user);
    }

    @Test
    void 유저_회원탈퇴_실패_비밀번호_불일치() {
        // given
        UserCheckPasswordRequestDto requestDto = new UserCheckPasswordRequestDto("correctPassword");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches("correctPassword", user.getPassword())).willReturn(false);

        // when
        UnauthorizedPasswordException exception = assertThrows(
                UnauthorizedPasswordException.class,
                () -> userService.deleteUser(userId, 1L, requestDto)
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("401 UNAUTHORIZED_PASSWORD 비밀번호를 확인해주세요.");
    }
}