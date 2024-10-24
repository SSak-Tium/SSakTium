package com.sparta.ssaktium.domain.dictionaries.repository;

import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
}
