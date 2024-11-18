//package com.sparta.ssaktium.domain.plants.plantDiaries.service;
//
//import com.sparta.ssaktium.domain.common.dto.AuthUser;
//import com.sparta.ssaktium.domain.common.service.S3Service;
//import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryRequestDto;
//import com.sparta.ssaktium.domain.plants.plantDiaries.dto.requestDto.PlantDiaryUpdateRequestDto;
//import com.sparta.ssaktium.domain.plants.plantDiaries.dto.responseDto.PlantDiaryResponseDto;
//import com.sparta.ssaktium.domain.plants.plantDiaries.entity.PlantDiary;
//import com.sparta.ssaktium.domain.plants.plantDiaries.exception.NotFoundPlantDiaryException;
//import com.sparta.ssaktium.domain.plants.plantDiaries.repository.PlantDiaryRepository;
//import com.sparta.ssaktium.domain.plants.plants.entity.Plant;
//import com.sparta.ssaktium.domain.plants.plants.service.PlantService;
//import com.sparta.ssaktium.domain.users.entity.User;
//import com.sparta.ssaktium.domain.users.enums.UserRole;
//import com.sparta.ssaktium.domain.users.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class PlantDiaryServiceTest {
//
//    @InjectMocks
//    private PlantDiaryService plantDiaryService;
//
//    @Mock
//    private PlantDiaryRepository plantDiaryRepository;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private S3Service s3Service;
//
//    @Mock
//    private PlantService plantService;
//
//    private AuthUser authUser;
//    private User user;
//    private Plant plant;
//    private PlantDiary plantDiary;
//
//    @BeforeEach
//    void setUp() {
//        // AuthUser 객체를 생성하고 User 객체로 변환
//        authUser = new AuthUser(1L, "user@test.com", UserRole.ROLE_USER);
//        user = User.fromAuthUser(authUser); // AuthUser를 이용하여 User 초기화
//        plant = new Plant(user, "Plant Name", "Plant Nickname", "imageUrl");
//        plantDiary = new PlantDiary(plant, "Diary Title", "Diary Content", "diaryImageUrl", LocalDate.now(), user);
//    }
//
//    @Nested
//    class 일기_생성_테스트 {
//        @Test
//        void 일기생성_성공() {
//            // Given
//            PlantDiaryRequestDto requestDto = new PlantDiaryRequestDto("Diary Title", "Diary Content", LocalDate.now());
//            MultipartFile image = new MockMultipartFile("image", new byte[0]); // Mock 이미지
//            given(userService.findUser(user.getId())).willReturn(user);
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(s3Service.uploadImageToS3(image, s3Service.bucket)).willReturn("uploadedImageUrl");
//            given(plantDiaryRepository.save(any(PlantDiary.class))).willReturn(plantDiary);
//
//            // When
//            PlantDiaryResponseDto response = plantDiaryService.createDiary(user.getId(), plant.getId(), requestDto, image);
//
//            // Then
//            assertThat(response).isNotNull();
//            assertThat(response.getTitle()).isEqualTo("Diary Title");
//        }
//    }
//
//    @Nested
//    class 일기_조회_테스트 {
//        @Test
//        void 모든일기조회_성공() {
//            // Given
//            Page<PlantDiary> plantDiaryPage = new PageImpl<>(List.of(plantDiary));
//            given(plantDiaryRepository.findAllByPlantIdAndUserId(plant.getId(), user.getId(), PageRequest.of(0, 10))).willReturn(plantDiaryPage);
//
//            // When
//            Page<PlantDiaryResponseDto> response = plantDiaryService.getAllDiaries(user.getId(), plant.getId(), 1, 10);
//
//            // Then
//            assertThat(response).isNotNull();
//            assertThat(response.getContent()).hasSize(1);
//        }
//
//        @Test
//        void 일기조회_일기가존재하지않는경우_예외발생() {
//            // Given
//            given(plantDiaryRepository.findAllByPlantIdAndUserId(plant.getId(), user.getId(), PageRequest.of(0, 10))).willReturn(Page.empty());
//
//            // When & Then
//            assertThrows(NotFoundPlantDiaryException.class,
//                    () -> plantDiaryService.getAllDiaries(user.getId(), plant.getId(), 1, 10));
//        }
//
//        @Test
//        void 특정일기조회_성공() {
//            // Given
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(plantDiaryRepository.findByIdAndUserId(plantDiary.getId(), user.getId())).willReturn(Optional.of(plantDiary));
//
//            // When
//            PlantDiaryResponseDto response = plantDiaryService.getDiary(user.getId(), plant.getId(), plantDiary.getId());
//
//            // Then
//            assertThat(response).isNotNull();
//            assertThat(response.getTitle()).isEqualTo(plantDiary.getTitle());
//        }
//
//        @Test
//        void 특정일기조회_일기가존재하지않는경우_예외발생() {
//            // Given
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(plantDiaryRepository.findByIdAndUserId(plantDiary.getId(), user.getId())).willReturn(Optional.empty());
//
//            // When & Then
//            assertThrows(NotFoundPlantDiaryException.class,
//                    () -> plantDiaryService.getDiary(user.getId(), plant.getId(), plantDiary.getId()));
//        }
//    }
//
//    @Nested
//    class 일기수정_테스트 {
//        @Test
//        void 일기수정_성공() {
//            // Given
//            PlantDiaryUpdateRequestDto requestDto = new PlantDiaryUpdateRequestDto("title", "content", LocalDate.now(), "updatedImageUrl");
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(plantDiaryRepository.findByIdAndUserId(plantDiary.getId(), user.getId())).willReturn(Optional.of(plantDiary));
//            given(s3Service.extractFileNameFromUrl(plantDiary.getImageUrl())).willReturn("diaryImageUrl");
//            doNothing().when(s3Service).deleteObject(s3Service.bucket, "diaryImageUrl");
//            given(plantDiaryRepository.save(any(PlantDiary.class))).willReturn(plantDiary);
//
//            // When
//            PlantDiaryResponseDto response = plantDiaryService.updateDiary(user.getId(), plant.getId(), plantDiary.getId(), requestDto);
//
//            // Then
//            assertThat(response).isNotNull();
//            assertThat(response.getContent()).isEqualTo("content");
//        }
//
//        @Test
//        void 일기수정_일기가존재하지않는경우_예외발생() {
//            // Given
//            PlantDiaryUpdateRequestDto requestDto = new PlantDiaryUpdateRequestDto("Updated Content", "content", LocalDate.now(), "updatedImageUrl");
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(plantDiaryRepository.findByIdAndUserId(plantDiary.getId(), user.getId())).willReturn(Optional.empty());
//
//            // When & Then
//            assertThrows(NotFoundPlantDiaryException.class,
//                    () -> plantDiaryService.updateDiary(user.getId(), plant.getId(), plantDiary.getId(), requestDto));
//        }
//    }
//
//    @Nested
//    class 일기삭제_테스트 {
//        @Test
//        void 일기삭제_성공() {
//            // Given
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(plantDiaryRepository.findByIdAndUserId(plantDiary.getId(), user.getId())).willReturn(Optional.of(plantDiary));
//            given(s3Service.extractFileNameFromUrl(plantDiary.getImageUrl())).willReturn("diaryImageUrl");
//            doNothing().when(s3Service).deleteObject(s3Service.bucket, "diaryImageUrl");
//
//            // When
//            plantDiaryService.deleteDiary(user.getId(), plant.getId(), plantDiary.getId());
//
//            // Then
//            verify(plantDiaryRepository).delete(plantDiary);
//        }
//
//        @Test
//        void 일기삭제_일기가존재하지않는경우_예외발생() {
//            // Given
//            given(plantService.findPlant(plant.getId(), user.getId())).willReturn(plant);
//            given(plantDiaryRepository.findByIdAndUserId(plantDiary.getId(), user.getId())).willReturn(Optional.empty());
//
//            // When & Then
//            assertThrows(NotFoundPlantDiaryException.class,
//                    () -> plantDiaryService.deleteDiary(user.getId(), plant.getId(), plantDiary.getId()));
//        }
//    }
//
//    @Nested
//    class 이미지업로드_테스트 {
//        @Test
//        void 이미지업로드_성공() {
//            // Given
//            MultipartFile image = new MockMultipartFile("image", new byte[0]); // Mock 이미지
//            given(s3Service.uploadImageToS3(image, s3Service.bucket)).willReturn("uploadedImageUrl");
//
//            // When
//            String uploadedUrl = plantDiaryService.uploadDiaryImage(image);
//
//            // Then
//            assertThat(uploadedUrl).isEqualTo("uploadedImageUrl");
//        }
//    }
//}