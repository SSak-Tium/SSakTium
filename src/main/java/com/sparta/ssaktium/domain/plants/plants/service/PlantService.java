package com.sparta.ssaktium.domain.plants.plants.service;

import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.plants.plants.dto.requestDto.PlantRequestDto;
import com.sparta.ssaktium.domain.plants.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.plants.plants.exception.NotFoundPlantException;
import com.sparta.ssaktium.domain.plants.plants.repository.PlantRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantService {

    private final PlantRepository plantRepository;
    private final S3Service s3Service;
    private final UserService userService;

    @Transactional
    public PlantResponseDto createPlant(Long userId,
                                        PlantRequestDto requestDto,
                                        MultipartFile image) {

        User user = userService.findUser(userId);

        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        Plant plant = new Plant(user, requestDto.getPlantName(), requestDto.getPlantNickname(),imageUrl);

        Plant savePlant = plantRepository.save(plant);

        return new PlantResponseDto(savePlant);
    }

    public PlantResponseDto getPlant(Long userId, Long id) {

        userService.findUser(userId);

        Plant plant = plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);

        return new PlantResponseDto(plant);
    }

    public List<PlantResponseDto> getAllPlants(Long userId) {

        User user = userService.findUser(userId);

        List<Plant> plantList = plantRepository.findByUser(user);

        return plantList.stream()
                .map(PlantResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlantResponseDto updatePlant(Long userId, Long id, PlantRequestDto requestDto, MultipartFile image) {

        userService.findUser(userId);

        Plant plant = plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);

        String imageName = s3Service.extractFileNameFromUrl(plant.getImageUrl());

        s3Service.deleteObject(s3Service.bucket, imageName);

        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        plant.update(requestDto.getPlantName(), requestDto.getPlantNickname(), imageUrl);

        plantRepository.save(plant);

        return new PlantResponseDto(plant);
    }

    @Transactional
    public String deletePlant(Long userId, Long id) {

        userService.findUser(userId);

        Plant plant = plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);

        String imageName = s3Service.extractFileNameFromUrl(plant.getImageUrl());

        s3Service.deleteObject(s3Service.bucket, imageName);

        plantRepository.delete(plant);

        return "정상적으로 삭제되었습니다.";
    }

    public Plant findPlant (Long id) {
        return plantRepository.findById(id).orElseThrow(NotFoundPlantException::new);
    }
}
