package com.sparta.springusersetting.domain.dictionaries.repository;

import com.sparta.springusersetting.domain.dictionaries.entitiy.Dictionaries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<Dictionaries, Long> {
}
