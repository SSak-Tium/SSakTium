package com.sparta.ssaktium.domain.dictionaries.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryResponseDto;
import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import com.sparta.ssaktium.domain.dictionaries.repository.DictionaryRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DictionaryServiceTest {

    @InjectMocks
    private DictionaryService dictionaryService;

    @Mock
    private DictionaryRepository dictionaryRepository;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    @Mock
    private MultipartFile image;

    private AuthUser authUser;
    private User user;
    private Dictionary dictionary;

    @BeforeEach
    void setUp(){
        authUser = mock(AuthUser.class);
        ReflectionTestUtils.setField(authUser, "userId", 1L);
        user = new User("email@gmail.com", "password", "name","0000", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        dictionary = new Dictionary("title", "content", "name", "https://image.url");
        ReflectionTestUtils.setField(dictionary, "id", 1L);
    }

    @Test
    void 식물도감_생성_성공() throws IOException {
        // given
        DictionaryRequestDto dictionaryRequestDto = new DictionaryRequestDto("test1", "content");

        given(userService.findUser(1L)).willReturn(user);
        given(s3Service.uploadImageToS3(any(MultipartFile.class), any())).willReturn(dictionary.getImageUrl());
        given(dictionaryRepository.save(any(Dictionary.class))).willReturn(dictionary);

        // when
        DictionaryResponseDto dictionaryResponseDto = dictionaryService.createDictionary(1L, dictionaryRequestDto, image);

        // then
        assertThat(dictionaryResponseDto.getTitle()).isEqualTo("test1");
    }

    @Test
    void getDictionary() {
    }

    @Test
    void getDictionaryList() {
    }

    @Test
    void updateDictionary() {
    }

    @Test
    void deleteDictionary() {
    }

    @Test
    void findDictionary() {
    }
}