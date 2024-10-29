package com.sparta.ssaktium.domain.dictionaries.repository;

import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import com.sparta.ssaktium.domain.dictionaries.entitiy.FavoriteDictionary;
import com.sparta.ssaktium.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteDictionaryRepository extends JpaRepository<FavoriteDictionary, Long> {
    Optional<FavoriteDictionary> findByUserAndDictionary(User user, Dictionary dictionary);

    @Query("SELECT fd.dictionary.id FROM FavoriteDictionary fd WHERE fd.user.id = :userId")
    List<Long> findFavoriteDictionaryIdsByUserId(@Param("userId") Long userId);
}
