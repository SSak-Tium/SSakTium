package com.sparta.ssaktium.domain.dictionaries.service;

import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.dictionaries.entitiy.Dictionaries;
import com.sparta.ssaktium.domain.dictionaries.repository.DictionaryRepository;
import com.sparta.ssaktium.domain.users.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

class DictionaryServiceTest {

    @InjectMocks
    private DictionaryService dictionaryService;

    @Mock
    private DictionaryRepository dictionaryRepository;

    private AuthUser authUser;
    private Dictionaries dictionaries;
    private Users users;

    @Test
    void createDictionary() {
    }

    @Test
    void getDictionary() {
    }

    @Test
    void getDictionaryList() {
    }

    @Test
    void updateDictionary() {
    }

    @Test
    void deleteDictionary() {
    }

    @Test
    void findDictionary() {
    }
}