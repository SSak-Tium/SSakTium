package com.sparta.ssaktium.domain.dictionaries.repository;

import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionaries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionaries, Long> {
}
