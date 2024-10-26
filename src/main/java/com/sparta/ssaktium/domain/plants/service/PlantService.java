package com.sparta.ssaktium.domain.plants.service;

import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.plants.dto.requestDto.PlantRequestDto;
import com.sparta.ssaktium.domain.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.entity.Plant;
import com.sparta.ssaktium.domain.plants.exception.NotFoundPlantException;
import com.sparta.ssaktium.domain.plants.repository.PlantRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantService {

    private final PlantRepository plantRepository;
    private final S3Service s3Service;
    private final UserService userService;

    public PlantResponseDto createPlant(Long userId,
                                        PlantRequestDto requestDto,
                                        MultipartFile image) throws IOException {

        User user = userService.findUser(userId);

        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        Plant plant = new Plant(requestDto, user, imageUrl);

        plantRepository.save(plant);

        return new PlantResponseDto(plant);
    }

    @Transactional(readOnly = true)
    public PlantResponseDto getPlant(Long userId, Long id) {

        User user = userService.findUser(userId);

        Plant plant = plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);

        return new PlantResponseDto(plant);
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDto> getAllPlants(Long userId) {

        User user = userService.findUser(userId);

        List<Plant> plantList = plantRepository.findByUserId(user);

        return plantList.stream()
                .map(PlantResponseDto::new)
                .collect(Collectors.toList());
    }

    public PlantResponseDto updatePlant(Long userId, Long id, PlantRequestDto requestDto, MultipartFile image) throws IOException {

        User user = userService.findUser(userId);

        Plant plant = plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);

        plant.update(requestDto);

        plantRepository.save(plant);

        return new PlantResponseDto(plant);
    }

    public String deltePlant(Long userId, Long id) {

        User user = userService.findUser(userId);

        Plant plant = plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);

        // 기존 등록된 URL 가지고 이미지 원본 이름 가져오기
        String imageName = s3Service.extractFileNameFromUrl(plant.getImageUrl());

        // 가져온 이미지 원본 이름으로 S3 이미지 삭제
        s3Service.s3Client.deleteObject(s3Service.bucket, imageName);

        plantRepository.delete(plant);

        return "정상적으로 삭제되었습니다.";
    }
}
