package com.sparta.springusersetting.domain.likes.boardLikes.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardLikesService {
}
