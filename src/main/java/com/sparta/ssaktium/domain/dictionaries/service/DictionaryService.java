package com.sparta.ssaktium.domain.dictionaries.service;

import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.request.DictionaryUpdateRequestDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryListResponseDto;
import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryResponseDto;
import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import com.sparta.ssaktium.domain.dictionaries.exception.NotFoundDictionaryException;
import com.sparta.ssaktium.domain.dictionaries.repository.DictionaryRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final UserService userService;
    private final S3Service s3Service;

    // 식물도감 등록
    public DictionaryResponseDto createDictionary(long userId, DictionaryRequestDto dictionaryRequestDto, MultipartFile image) throws IOException {
        // 유저 조회
        User user = userService.findUser(userId);

        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        // Entity 생성
        Dictionary dictionary = new Dictionary(dictionaryRequestDto, user.getUserName(), imageUrl);

        // DB 저장
        dictionaryRepository.save(dictionary);

        // Dto 반환
        return new DictionaryResponseDto(dictionary);
    }

    // 식물도감 단건 조회
    @Transactional(readOnly = true)
    public DictionaryResponseDto getDictionary(long userId, long dictionaryId) {
        // 유저 조회
        userService.findUser(userId);

        // 식물도감 조회
        Dictionary dictionary = findDictionary(dictionaryId);

        // Dto 반환
        return new DictionaryResponseDto(dictionary);
    }

    // 식물도감 리스트 조회
    @Transactional(readOnly = true)
    public Page<DictionaryListResponseDto> getDictionaryList(int page, int size) {
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page - 1, size);

        // 식물도감 전체 조회
        Page<Dictionary> dictionariesPage = dictionaryRepository.findAll(pageable);

        // Dto 변환
        Page<DictionaryListResponseDto> dtoPage = dictionariesPage.map(dictionary -> {
           DictionaryListResponseDto dictionaryListResponseDto = new DictionaryListResponseDto(dictionary.getTitle(), dictionary.getUserName());
           return dictionaryListResponseDto;
        });
        return dtoPage;
    }

    // 식물도감 수정
    public DictionaryResponseDto updateDictionary(long userId, DictionaryUpdateRequestDto dictionaryUpdateRequestDto, MultipartFile image, long dictionaryId) throws IOException {
        // 유저 조회
        userService.findUser(userId);

        // 식물도감 조회
        Dictionary dictionary = findDictionary(dictionaryId);

        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        // Entity 수정
        dictionary.update(dictionaryUpdateRequestDto, imageUrl);

        // DB 저장
        dictionaryRepository.save(dictionary);

        // DTO 반환
        return new DictionaryResponseDto(dictionary);
    }

    // 식물도감 삭제
    public String deleteDictionary(long userId, long dictionaryId) {
        // 유저 조회
        userService.findUser(userId);

        // 식물도감 조회
        Dictionary dictionary = findDictionary(dictionaryId);

        // 기존 등록된 URL 가지고 이미지 원본 이름 가져오기
        String ImageName = s3Service.extractFileNameFromUrl(dictionary.getImageUrl());

        // 가져온 이미지 원본 이름으로 S3 이미지 삭제
        s3Service.s3Client.deleteObject(s3Service.bucket, ImageName);

        // DB 삭제
        dictionaryRepository.delete(dictionary);

        return "정상적으로 삭제되었습니다.";
    }

    // 식물도감 조회 메서드
    public Dictionary findDictionary(long dictionaryId) {
        return dictionaryRepository.findById(dictionaryId).orElseThrow(NotFoundDictionaryException::new);
    }
}
