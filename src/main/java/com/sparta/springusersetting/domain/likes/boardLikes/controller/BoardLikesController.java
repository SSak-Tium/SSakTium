package com.sparta.springusersetting.domain.likes.boardLikes.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RequiredArgsConstructor
@RestController("/v1/boards/{id}/likes")
public class BoardLikesController {

}
