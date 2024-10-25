package com.sparta.ssaktium.domain.plants.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.plants.dto.requestDto.PlantRequestDto;
import com.sparta.ssaktium.domain.plants.dto.responseDto.PlantResponseDto;
import com.sparta.ssaktium.domain.plants.entity.Plant;
import com.sparta.ssaktium.domain.plants.exception.NotFoundPlantException;
import com.sparta.ssaktium.domain.plants.repository.PlantRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    @InjectMocks
    private PlantService plantService;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private UserService userService;

    @Mock
    private MultipartFile multipartFile;

    private User user;
    private AuthUser authUser;
    private Plant plant;
    private PlantRequestDto plantRequestDto;
    private String imageUrl;

    @BeforeEach
    void setUp() {
        // 초기 설정
        user = new User("test@example.com", "password", "testUser", "1997", UserRole.ROLE_ADMIN);
        authUser = new AuthUser(1L, "test@example.com", UserRole.ROLE_ADMIN);
        plantRequestDto = new PlantRequestDto("testPlant", "testNickname");
        plant = new Plant(plantRequestDto, user, "testUrl");
        imageUrl = "testImageUrl";
    }

    @Test
    void 식물_조회_성공() {
        // given
        when(userService.findUser(anyLong())).thenReturn(user);
        when(plantRepository.findById(anyLong())).thenReturn(Optional.of(plant));

        // when
        PlantResponseDto response = plantService.getPlant(authUser, 1L);

        // then
        assertEquals(plant.getPlantName(), response.getPlantName());
        verify(plantRepository).findById(anyLong());
    }

    @Test
    void 식물_조회_실패_찾을수없음() {
        // given
        when(userService.findUser(anyLong())).thenReturn(user);
        when(plantRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThrows(NotFoundPlantException.class, () -> plantService.getPlant(authUser, 1L));
    }

    @Test
    void 모든_식물_조회_성공() {
        // given
        when(userService.findUser(anyLong())).thenReturn(user);
        when(plantRepository.findByUserId(any(User.class))).thenReturn(List.of(plant));

        // when
        List<PlantResponseDto> response = plantService.getAllPlants(authUser);

        // then
        assertEquals(1, response.size());
        assertEquals(plant.getPlantName(), response.get(0).getPlantName());
        verify(plantRepository).findByUserId(any(User.class));
    }

    @Test
    void 식물_수정_성공() throws IOException {
        // given
        when(userService.findUser(anyLong())).thenReturn(user);
        when(plantRepository.findById(anyLong())).thenReturn(Optional.of(plant));

        // when
        PlantResponseDto response = plantService.updatePlant(authUser, 1L, plantRequestDto, multipartFile);

        // then
        assertEquals(plantRequestDto.getPlantName(), response.getPlantName());
        verify(plantRepository).save(any(Plant.class));
    }
}
