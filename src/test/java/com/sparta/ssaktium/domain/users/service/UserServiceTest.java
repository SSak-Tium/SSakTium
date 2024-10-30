package com.sparta.ssaktium.domain.users.service;

import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.dictionaries.repository.FavoriteDictionaryRepository;
import com.sparta.ssaktium.domain.users.dto.response.UserResponseDto;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import jakarta.persistence.EntityManager;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private S3Service s3Service;
    @Mock
    private FavoriteDictionaryRepository favoriteDictionaryRepository;

    private User user;
    private long userId;
    private User savedUser;
    private String encodedPassword;
    private List<Long> favoriteDictionaries;


    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User("email@gmail.com", "password", "name", "0000", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "createdAt", LocalDateTime.now());
        encodedPassword = "encodedPassword";
        savedUser = new User("email", "password", "name", "1997", UserRole.ROLE_USER);
        favoriteDictionaries = List.of(1L, 2L);
    }

    @Test
    void 사용자_조회_성공() {
        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(favoriteDictionaryRepository.findFavoriteDictionaryIdsByUserId(anyLong())).willReturn(favoriteDictionaries);

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