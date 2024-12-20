package com.sparta.ssaktium.domain.plants.plantDiaries.service;

import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryUpdateRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import com.sparta.ssaktium.domain.plants.plantDiaries.exception.NotFoundPlantDiaryException;
import com.sparta.ssaktium.domain.plants.plantDiaries.repository.PlantDiaryRepository;
import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.plants.plants.service.PlantService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantDiaryService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final PlantService plantService;

    @Transactional
    public PlantDiaryResponseDto createDiary(Long userId, Long id, PlantDiaryRequestDto requestDto, MultipartFile image) {

        User user = userService.findUser(userId);

        Plant plant = plantService.findPlant(id, userId);

        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        PlantDiary plantDiary = new PlantDiary(
                plant,
                requestDto.getTitle(),
                requestDto.getContent(),
                imageUrl,
                requestDto.getItemDate(),
                user);

        PlantDiary savedPlantDiary = plantDiaryRepository.save(plantDiary);

        return new PlantDiaryResponseDto(savedPlantDiary);
    }

    public Page<PlantDiaryResponseDto> getAllDiaries(Long userId, Long id, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<PlantDiary> plantDiaryPage = plantDiaryRepository.findAllByPlantIdAndUserId(id, userId, pageable);

        if (plantDiaryPage.isEmpty()) {
            throw new NotFoundPlantDiaryException();
        }

        return plantDiaryPage.map(PlantDiaryResponseDto::new);
    }

    public PlantDiaryResponseDto getDiary(Long userId, Long id, Long diaryId) {

        plantService.findPlant(id, userId);

        PlantDiary plantDiary = plantDiaryRepository.findByIdAndUserId(diaryId, userId).orElseThrow(NotFoundPlantDiaryException::new);

        return new PlantDiaryResponseDto(plantDiary);
    }

    @Transactional
    public PlantDiaryResponseDto updateDiary(Long userId, Long id, Long diaryId, PlantDiaryUpdateRequestDto requestDto) {

        plantService.findPlant(id, userId);

        PlantDiary plantDiary = plantDiaryRepository.findByIdAndUserId(diaryId, userId).orElseThrow(NotFoundPlantDiaryException::new);

        String imageName = s3Service.extractFileNameFromUrl(plantDiary.getImageUrl());

        s3Service.deleteObject(s3Service.bucket, imageName);

        plantDiary.update(requestDto.getContent(), requestDto.getItemDate(), requestDto.getImageUrl());

        plantDiaryRepository.save(plantDiary);

        return new PlantDiaryResponseDto(plantDiary);
    }

    @Transactional
    public void deleteDiary(Long userId, Long id, Long diaryId) {

        plantService.findPlant(id, userId);

        PlantDiary plantDiary = plantDiaryRepository.findByIdAndUserId(diaryId, userId).orElseThrow(NotFoundPlantDiaryException::new);

        String imageName = s3Service.extractFileNameFromUrl(plantDiary.getImageUrl());

        s3Service.deleteObject(s3Service.bucket, imageName);

        plantDiaryRepository.delete(plantDiary);
    }

    public String uploadDiaryImage(MultipartFile image) {

        return s3Service.uploadImageToS3(image, s3Service.bucket);
    }
}
