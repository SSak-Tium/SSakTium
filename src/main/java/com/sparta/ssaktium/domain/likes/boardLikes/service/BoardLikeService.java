package com.sparta.ssaktium.domain.likes.boardLikes.service;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikeResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLike;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.LikeOwnerMismatchException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikeException;
import com.sparta.ssaktium.domain.likes.boardLikes.repository.BoardLikeRepository;
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


    // 게시글 좋아요 조회 = 필요없는 기능일수도!!
    public BoardLikeResponseDto getBoardLikes(Long boardId, AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        return new BoardLikeResponseDto(boardId,board.getBoardLikesCount());
    }

    // 게시글에 좋아요 등록
    @Transactional
    public BoardLikeResponseDto postBoardLikes(Long boardId, AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        // 좋아요를 이미 누른 게시글인지 확인
        if (boardLikeRepository.existsByBoardIdAndUserId(boardId,authUser.getUserId())){
            throw new AlreadyLikedException();
        }

        // 좋아요 등록
        BoardLike boardLike = new BoardLike(board, authUser.getUserId());
        boardLikeRepository.save(boardLike);

        // 게시글에 등록된 좋아요 수 증가
        board.incrementLikesCount();
        boardRepository.save(board);

        return new BoardLikeResponseDto(boardId,board.getBoardLikesCount());
    }

    // 게시글에 좋아요 취소
    @Transactional
    public void deleteBoardLikes(Long boardId, Long likeId, AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        // 게시글에 해당 유저의 좋아요가 존재하는지 확인
        if (!boardLikeRepository.existsByBoardIdAndUserId(boardId,authUser.getUserId())){
            throw new NotFoundBoardLikeException();
        }

        // 좋아요가 존재하는지 확인
        BoardLike boardLike = boardLikeRepository.findById(likeId)
                .orElseThrow(()-> new NotFoundBoardLikeException());

        // 좋아요 한 유저가 맞는지 확인
        if(!boardLike.getUserId().equals(authUser.getUserId())){
            throw new LikeOwnerMismatchException();
        }

        // 좋아요 취소
        boardLikeRepository.delete(boardLike);
        board.decrementLikesCount();
    }
}
