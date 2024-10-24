package com.sparta.ssaktium.domain.plants.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
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

    public PlantResponseDto createPlant(AuthUser authUser,
                                        PlantRequestDto requestDto,
                                        MultipartFile image) throws IOException {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        String imageUrl = s3Service.uploadImageToS3(image, s3Service.bucket);

        Plant plant = new Plant(requestDto, user, imageUrl);

        plantRepository.save(plant);

        return new PlantResponseDto(plant.getId(), plant.getUserId().getId(), plant.getPlantName(), plant.getPlantNickname(), plant.getImageUrl());
    }

    @Transactional(readOnly = true)
    public PlantResponseDto getPlant(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Plant plant = plantRepository.findById(id).orElseThrow(() ->
                new NotFoundPlantException());

        return new PlantResponseDto(
                plant.getId(),
                plant.getUserId().getId(),
                plant.getPlantName(),
                plant.getPlantNickname(),
                plant.getImageUrl());
    }

    @Transactional(readOnly = true)
    public List<PlantResponseDto> getAllPlants(AuthUser authUser) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        List<Plant> plantList = plantRepository.findByUserId(user);

        return plantList.stream()
                .map(plant -> new PlantResponseDto(
                        plant.getId(),
                        plant.getUserId().getId(),
                        plant.getPlantName(),
                        plant.getPlantNickname(),
                        plant.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    public PlantResponseDto updatePlant(AuthUser authUser, Long id, PlantRequestDto requestDto, MultipartFile image) throws IOException {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Plant plant = plantRepository.findById(id).orElseThrow(() ->
                new NotFoundPlantException());

        plant.update(requestDto);

        plantRepository.save(plant);

        return new PlantResponseDto(
                plant.getId(),
                plant.getUserId().getId(),
                plant.getPlantName(),
                plant.getPlantNickname(),
                plant.getImageUrl());
    }

    public String deltePlant(AuthUser authUser, Long id) {

        User user = User.fromAuthUser(authUser);
        userService.findUser(user.getId());

        Plant plant = plantRepository.findById(id).orElseThrow(() ->
                new NotFoundPlantException());

        plantRepository.delete(plant);

        return "정상적으로 삭제되었습니다.";
    }
}
