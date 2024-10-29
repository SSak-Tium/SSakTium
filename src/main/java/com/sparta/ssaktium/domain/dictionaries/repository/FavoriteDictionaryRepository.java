package com.sparta.ssaktium.domain.dictionaries.repository;

import com.sparta.ssaktium.domain.dictionaries.entitiy.FavoriteDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteDictionaryRepository extends JpaRepository<FavoriteDictionary, Long> {
}
