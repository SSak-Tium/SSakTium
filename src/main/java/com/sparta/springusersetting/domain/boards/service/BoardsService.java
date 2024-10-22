package com.sparta.springusersetting.domain.boards.service;

import com.sparta.springusersetting.domain.boards.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardsService {

    private final BoardsRepository boardsRepository;
}
