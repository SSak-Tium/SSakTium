package com.sparta.ssaktium.domain.dictionaries.service;

import com.sparta.ssaktium.domain.dictionaries.dto.response.DictionaryImageResponseDto;
import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import com.sparta.ssaktium.domain.dictionaries.entitiy.FavoriteDictionary;
import com.sparta.ssaktium.domain.dictionaries.repository.FavoriteDictionaryRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteDictionaryService {
    private final UserService userService;
    private final DictionaryService dictionaryService;
    private final FavoriteDictionaryRepository favoriteDictionaryRepository;

    // 식물도감 관심 등록
    public String pushFavoriteDictionary(long userId, long dictionaryId) {
        // 유저 조회
        User user = userService.findUser(userId);

        // 식물도감 조회
        Dictionary dictionary = dictionaryService.findDictionary(dictionaryId);

        FavoriteDictionary favoriteDictionary = FavoriteDictionary.addFavoriteDictionary(user, dictionary);

        favoriteDictionaryRepository.save(favoriteDictionary);

        return dictionary.getTitle() + "를 관심등록 했습니다.";
    }
}
