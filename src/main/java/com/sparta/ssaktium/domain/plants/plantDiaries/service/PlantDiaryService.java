package com.sparta.ssaktium.domain.plants.plantDiaries.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.PlantDiaryRequestDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
import com.sparta.ssaktium.domain.plants.plantDiaries.exception.NotFoundPlantDiaryException;
import com.sparta.ssaktium.domain.plants.plantDiaries.repository.PlantDiaryRepository;
import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.plants.plants.service.PlantService;
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
@Transactional(readOnly = true)
public class PlantDiaryService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final PlantService plantService;

    @Transactional
    public PlantDiaryResponseDto createDiary(Long userId, Long id, PlantDiaryRequestDto requestDto, MultipartFile image) throws IOException {
        userService.findUser(userId);

        Plant plant = plantService.findPlant(id);

        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        PlantDiary plantDiary = new PlantDiary(plant, requestDto.getContent(), imageUrl, requestDto.getItemDate());

        PlantDiary savedPlantDiary = plantDiaryRepository.save(plantDiary);

        return new PlantDiaryResponseDto(savedPlantDiary);
    }

    public Page<PlantDiaryResponseDto> getAllDiaries(Long userId, Long id, int page, int size) {

        userService.findUser(userId);

        Pageable pageable = PageRequest.of(page, size);

        Page<PlantDiary> plantDiaryPage = plantDiaryRepository.findAllByPlantId(id, pageable);

        return plantDiaryPage.map(PlantDiaryResponseDto::new);

    }

    public PlantDiaryResponseDto getDiary(long userId, Long id, Long diaryId) {

        userService.findUser(userId);

        PlantDiary plantDiary = plantDiaryRepository.findById(diaryId).orElseThrow(NotFoundPlantDiaryException::new);

        return new PlantDiaryResponseDto(plantDiary);
    }


}
