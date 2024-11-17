package com.sparta.ssaktium.domain.likes.boardLikes.service;

import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.likes.LikeDbConsumer;
import com.sparta.ssaktium.domain.likes.LikeEventProducer;
import com.sparta.ssaktium.domain.likes.LikeRedisService;
import com.sparta.ssaktium.domain.likes.boardLikes.BoardLikeEvent;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikeResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.repository.BoardLikeRepository;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardLikeService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final LikeEventProducer likeProducer; // 카프카 이벤트 프로듀서 주입
    private final LikeRedisService likeRedisService; // 레디스 좋아요 수 조회용
    private final LikeDbConsumer likeDbConsumer;

    // 게시글에 좋아요 등록
    @Transactional
    public BoardLikeResponseDto postBoardLikes(Long userId, Long boardId) {
        // 게시글이 있는지 확인
        boardRepository.findById(boardId).orElseThrow(() -> new NotFoundBoardException());

        // 좋아요를 이미 누른 게시글인지 확인
        if (likeRedisService.isLiked(
                LikeRedisService.TARGET_TYPE_BOARD,
                boardId.toString(),
                userId.toString())) {
            throw new AlreadyLikedException();
        }

        // 좋아요 등록(Kafka -> Redis)
        likeProducer.sendLikeEvent(new BoardLikeEvent(
                userId.toString(), boardId.toString(), "LIKE"));

        // 좋아요 수 레디스에서 반영
        int redisLikeCount = likeRedisService.getRedisLikeCount(
                LikeRedisService.TARGET_TYPE_BOARD, boardId.toString());

        return new BoardLikeResponseDto(boardId, redisLikeCount);
    }

    // 게시글에 좋아요 취소
    @Transactional
    public void deleteBoardLikes(Long userId, Long boardId) {
        // 게시글이 있는지 확인
        boardRepository.findById(boardId).orElseThrow(() -> new NotFoundBoardException());

        // 게시글에 해당 유저의 좋아요가 있는지 확인
        if (likeRedisService.isLiked(
                LikeRedisService.TARGET_TYPE_BOARD, boardId.toString(), userId.toString())) {
            throw new NotFoundBoardLikeException();
        }

        // 카프카 좋아요 취소 이벤트
        likeProducer.sendLikeEvent(new BoardLikeEvent(
                userId.toString(), boardId.toString(), "CANCEL"));
    }
}
