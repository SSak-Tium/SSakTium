package com.sparta.ssaktium.domain.likes.boardLikes.service;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.likes.LikeEventProducer;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikeResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLike;
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

    // 게시글에 좋아요 등록
    @Transactional
    public BoardLikeResponseDto postBoardLikes(Long userId, Long boardId) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        // 좋아요를 이미 누른 게시글인지 확인
        if (boardLikeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new AlreadyLikedException();
        }
        // 카프카 좋아요 등록 이벤트
        likeProducer.sendBoardLikeEvent(userId.toString(), boardId.toString(), "LIKE");

        // 좋아요 등록
        BoardLike boardLike = new BoardLike(board, userId);
        boardLikeRepository.save(boardLike);

        // 게시글에 등록된 좋아요 수 증가
        board.incrementLikesCount();
        boardRepository.save(board);

        return new BoardLikeResponseDto(boardId, board.getBoardLikesCount());
    }

    // 게시글에 좋아요 취소
    @Transactional
    public void deleteBoardLikes(Long userId, Long boardId) {
        // 게시글의 좋아요 수를 줄이기 위함
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        // 게시글에 해당 유저의 좋아요가 있는지 확인
        BoardLike boardLike = boardLikeRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new NotFoundBoardLikeException());

        // 카프카 좋아요 취소 이벤트
        likeProducer.sendBoardLikeEvent(userId.toString(), boardId.toString(), "CANCEL");

        // 좋아요 취소
        boardLikeRepository.delete(boardLike);
        board.decrementLikesCount();

        // 게시글에 등록된 좋아요 수 감소
        boardRepository.save(board);
    }
}
