package com.sparta.springusersetting.domain.boards.controller;

import com.sparta.springusersetting.domain.boards.service.BoardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;
}
