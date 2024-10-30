package com.sparta.ssaktium.domain.plants.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.common.exception.UauthorizedAccessException;
import com.sparta.ssaktium.domain.common.service.S3Service;
import com.sparta.ssaktium.domain.plants.plants.dto.requestDto.PlantUpdateRequestDto;
import com.sparta.ssaktium.domain.plants.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
import com.sparta.ssaktium.domain.plants.plants.exception.NotFoundPlantException;
import com.sparta.ssaktium.domain.plants.plants.repository.PlantRepository;
import com.sparta.ssaktium.domain.plants.plants.service.PlantService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    @InjectMocks
    private PlantService plantService;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserService userService;

    private AuthUser authUser;
    private User user;
    private Plant plant;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "user1@test.com", UserRole.ROLE_USER);
        user = User.fromAuthUser(authUser);
        plant = new Plant(user, "Plant Name", "Plant Nickname", "imageUrl");
    }


    @Nested
    class 식물조회_테스트 {
        @Test
        void 식물조회_성공() {
            // Given
            given(plantRepository.findByPlantIdAndUserId(plant.getId(), user.getId())).willReturn(Optional.of(plant));

            // When
            PlantResponseDto response = plantService.getPlant(user.getId(), plant.getId());

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getPlantName()).isEqualTo(plant.getPlantName());
        }

        @Test
        void 존재하지않는_식물조회시_예외발생() {
            // Given
            given(plantRepository.findByPlantIdAndUserId(plant.getId(), user.getId())).willReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundPlantException.class, () -> plantService.getPlant(user.getId(), plant.getId()));
        }
    }

    @Nested
    class 식물목록조회_테스트 {
        @Test
        void 식물목록조회_성공() {
            // Given
            List<Plant> plantList = Arrays.asList(plant);
            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(plantRepository.findAllByUser(user)).willReturn(plantList);

            // When
            List<PlantResponseDto> response = plantService.getAllPlants(authUser.getUserId());

            // Then
            assertThat(response).isNotNull();
            assertThat(response).hasSize(1);
            assertThat(response.get(0).getPlantName()).isEqualTo(plant.getPlantName());
        }

        @Test
        void 식물목록이_없을때_예외발생() {
            // Given
            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(plantRepository.findAllByUser(user)).willReturn(Arrays.asList());

            // When & Then
            assertThrows(UauthorizedAccessException.class, () -> plantService.getAllPlants(authUser.getUserId()));
        }
    }

    @Nested
    class 식물수정_테스트 {
        @Test
        void 식물수정_성공() {
            // Given
            PlantUpdateRequestDto requestDto = new PlantUpdateRequestDto("New Plant Name", "New Plant Nickname", "newImageUrl");
            given(plantRepository.findByPlantIdAndUserId(plant.getId(), user.getId())).willReturn(Optional.of(plant));
            given(s3Service.extractFileNameFromUrl(plant.getImageUrl())).willReturn("imageName");
            doNothing().when(s3Service).deleteObject(s3Service.bucket, "imageName");
            given(plantRepository.save(any(Plant.class))).willReturn(plant);

            // When
            PlantResponseDto response = plantService.updatePlant(user.getId(), plant.getId(), requestDto);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getPlantName()).isEqualTo(requestDto.getPlantName());
        }

        @Test
        void 존재하지않는_식물수정시_예외발생() {
            // Given
            PlantUpdateRequestDto requestDto = new PlantUpdateRequestDto("New Plant Name", "New Plant Nickname", "newImageUrl");
            given(plantRepository.findByPlantIdAndUserId(plant.getId(), user.getId())).willReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundPlantException.class, () -> plantService.updatePlant(user.getId(), plant.getId(), requestDto));
        }
    }

    @Nested
    class 식물삭제_테스트 {
        @Test
        void 식물삭제_성공() {
            // Given
            given(plantRepository.findByPlantIdAndUserId(plant.getId(), user.getId())).willReturn(Optional.of(plant));
            given(s3Service.extractFileNameFromUrl(plant.getImageUrl())).willReturn("imageName");
            doNothing().when(s3Service).deleteObject(s3Service.bucket, "imageName");

            // When
            plantService.deletePlant(user.getId(), plant.getId());

            // Then
            verify(plantRepository).delete(plant);
        }
        @Test
        void 존재하지않는_식물삭제시_예외발생() {
            // Given
            given(plantRepository.findByPlantIdAndUserId(plant.getId(), user.getId())).willReturn(Optional.empty());

            // When & Then
            assertThrows(NotFoundPlantException.class, () -> plantService.deletePlant(user.getId(), plant.getId()));
        }
    }

}
