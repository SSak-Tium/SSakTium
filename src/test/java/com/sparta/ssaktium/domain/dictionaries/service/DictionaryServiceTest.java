package com.sparta.ssaktium.domain.dictionaries.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryListResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    private AmazonS3Client s3Client;
    @Mock
    private MultipartFile image;

    private AuthUser authUser;
    private User user;
    private Dictionary dictionary;
    private String imageUrl;
    private long userId;
    private long dictionaryId;

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(s3Service, "s3Client", s3Client);
        userId = 1L;
        dictionaryId = 1L;
        authUser = mock(AuthUser.class);
        ReflectionTestUtils.setField(authUser, "userId", 1L);
        user = new User("email@gmail.com", "password", "name","0000", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        dictionary = Dictionary.addDictionary("title", "content", user, "https://image.url");
        ReflectionTestUtils.setField(dictionary, "id", 1L);
        imageUrl = "https://image.url";
    }

    @Test
    void 식물도감_생성_성공() {
        // given
        DictionaryRequestDto requestDto = new DictionaryRequestDto("title", "content");

        given(userService.findUser(1L)).willReturn(user);
        given(s3Service.uploadImageToS3(any(MultipartFile.class), any())).willReturn(imageUrl);
        given(dictionaryRepository.save(any(Dictionary.class))).willReturn(dictionary);

        // when
        DictionaryResponseDto responseDto = dictionaryService.createDictionary(1L, requestDto, image);

        // then
        assertThat(responseDto.getTitle()).isEqualTo("title");
    }

    @Test
    void 식물도감_단건조회_성공() {
        // given

        given(userService.findUser(anyLong())).willReturn(user);
        given(dictionaryRepository.findById(anyLong())).willReturn(Optional.of(dictionary));

        // when
        DictionaryResponseDto responseDto = dictionaryService.getDictionary(dictionaryId);

        // then
        assertThat(responseDto.getTitle()).isEqualTo("title");
    }

    @Test
    void 식물도감_리스트_조회_성공() {
        // given
        int page = 1;
        int size = 5;

        List<Dictionary> dictionaries = List.of(
                Dictionary.addDictionary("title1", "content1", user, "https://image1.url"),
                Dictionary.addDictionary("title2", "content2", user, "https://image2.url")
        );

        Page<Dictionary> dictionaryPage = new PageImpl<>(dictionaries);

        given(dictionaryRepository.findAll(any(Pageable.class))).willReturn(dictionaryPage);

        // when
        Page<DictionaryListResponseDto> responsePage = dictionaryService.getDictionaryList(page, size);

        // then
        assertThat(responsePage.getContent()).hasSize(2);
        assertThat(responsePage.getContent().get(0).getTitle()).isEqualTo("title1");
    }


    @Test
    void 식물도감_수정_성공() throws IOException {
        // given
        DictionaryUpdateRequestDto requestDto = new DictionaryUpdateRequestDto(imageUrl,"new title", "content");
        given(userService.findUser(1L)).willReturn(user);
        given(dictionaryRepository.findById(anyLong())).willReturn(Optional.of(dictionary));
        given(s3Service.uploadImageToS3(any(MultipartFile.class), any())).willReturn(imageUrl);

        dictionary.update(requestDto);
        given(dictionaryRepository.save(any(Dictionary.class))).willReturn(dictionary);

        // when
        DictionaryResponseDto responseDto = dictionaryService.updateDictionary(requestDto, dictionaryId);

        assertThat(responseDto.getTitle()).isEqualTo("new title");
    }

    @Test
    void 식물도감_삭제_성공() {
        // given
        String imageName = "image.jpg";
        given(userService.findUser(1L)).willReturn(user);
        given(dictionaryRepository.findById(anyLong())).willReturn(Optional.of(dictionary));
        given(s3Service.extractFileNameFromUrl(dictionary.getImageUrl())).willReturn(imageName);

        // when
        String result = dictionaryService.deleteDictionary(dictionaryId);

        // then
        assertThat(result).isEqualTo("정상적으로 삭제되었습니다.");
        verify(s3Client).deleteObject(s3Service.bucket, imageName);
        verify(dictionaryRepository).delete(dictionary);
    }
}