package com.sparta.ssaktium.domain.dictionaries.service;

import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import com.sparta.ssaktium.domain.dictionaries.entitiy.FavoriteDictionary;
import com.sparta.ssaktium.domain.dictionaries.repository.FavoriteDictionaryRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteDictionaryService {
    private final UserService userService;
    private final DictionaryService dictionaryService;
    private final FavoriteDictionaryRepository favoriteDictionaryRepository;

    // 식물도감 관심 등록,해제
    public String toggleFavoriteDictionary(long userId, long dictionaryId) {
        // 유저 조회
        User user = userService.findUser(userId);

        // 식물도감 조회
        Dictionary dictionary = dictionaryService.findDictionary(dictionaryId);

        // 관심 등록 여부 확인
        Optional<FavoriteDictionary> existingFavorite =
                favoriteDictionaryRepository.findByUserAndDictionary(user, dictionary);

        if (existingFavorite.isPresent()) {
            // 이미 등록된 경우 관심 해제
            favoriteDictionaryRepository.delete(existingFavorite.get());
            return dictionary.getTitle() + "의 관심등록을 해제했습니다.";
        } else {
            // 등록되지 않은 경우 관심 등록
            FavoriteDictionary favoriteDictionary = FavoriteDictionary.addFavoriteDictionary(user, dictionary);
            favoriteDictionaryRepository.save(favoriteDictionary);
            return dictionary.getTitle() + "을(를) 관심등록 했습니다.";
        }
    }

    // 관심 식물도감 찾는 메서드
    public List<Long> findFavoriteDictionary(long userId) {
        return favoriteDictionaryRepository.findFavoriteDictionaryIdsByUserId(userId);
    }
}
