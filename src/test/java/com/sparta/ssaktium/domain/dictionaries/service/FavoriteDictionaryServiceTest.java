//package com.sparta.ssaktium.domain.dictionaries.service;
//
//import com.sparta.ssaktium.domain.common.dto.AuthUser;
//import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
//import com.sparta.ssaktium.domain.dictionaries.entitiy.FavoriteDictionary;
//import com.sparta.ssaktium.domain.dictionaries.repository.FavoriteDictionaryRepository;
//import com.sparta.ssaktium.domain.users.entity.User;
//import com.sparta.ssaktium.domain.users.enums.UserRole;
//import com.sparta.ssaktium.domain.users.repository.UserRepository;
//import com.sparta.ssaktium.domain.users.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//class FavoriteDictionaryServiceTest {
//
//    @InjectMocks
//    private FavoriteDictionaryService favoriteDictionaryService;
//
//    @Mock
//    private UserService userService;
//    @Mock
//    private DictionaryService dictionaryService;
//    @Mock
//    private FavoriteDictionaryRepository favoriteDictionaryRepository;
//
//    private AuthUser authUser;
//    private User user;
//    private Dictionary dictionary;
//    private long userId;
//    private long dictionaryId;
//
//    @BeforeEach
//    void setUp() {
//        userId = 1L;
//        dictionaryId = 1L;
//        authUser = mock(AuthUser.class);
//        ReflectionTestUtils.setField(authUser, "userId", 1L);
//        user = new User("email@gmail.com", "password", "name", "0000", UserRole.ROLE_USER, "socialId");
//        ReflectionTestUtils.setField(user, "id", 1L);
//        dictionary = Dictionary.addDictionary("title", "content", user, "https://image.url");
//        ReflectionTestUtils.setField(dictionary, "id", 1L);
//    }
//
//    @Test
//    void 식물도감_관심등록_성공() {
//        // given
//        given(userService.findUser(anyLong())).willReturn(user);
//        given(dictionaryService.findDictionary(anyLong())).willReturn(dictionary);
//        given(favoriteDictionaryRepository.findByUserAndDictionary(user, dictionary)).willReturn(Optional.empty());
//
//        // when
//        String result = favoriteDictionaryService.toggleFavoriteDictionary(userId, dictionaryId);
//
//        // then
//        assertThat(result).isEqualTo(dictionary.getTitle() + "을(를) 관심등록 했습니다.");
//    }
//
//    @Test
//    void 식물도감_관심해제_성공() {
//        // given
//        FavoriteDictionary existingFavorite = FavoriteDictionary.addFavoriteDictionary(user, dictionary);
//        given(userService.findUser(anyLong())).willReturn(user);
//        given(dictionaryService.findDictionary(dictionaryId)).willReturn(dictionary);
//        given(favoriteDictionaryRepository.findByUserAndDictionary(user, dictionary)).willReturn(Optional.of(existingFavorite));
//
//        // when
//        String result = favoriteDictionaryService.toggleFavoriteDictionary(userId, dictionaryId);
//
//        // then
//        assertThat(result).isEqualTo(dictionary.getTitle() + "의 관심등록을 해제했습니다.");
//    }
//}