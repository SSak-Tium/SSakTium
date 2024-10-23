package com.sparta.ssaktium.domain.likes.boardLikes.service;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikesResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLikes;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.LikeOwnerMismatchException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikesException;
import com.sparta.ssaktium.domain.likes.boardLikes.repository.BoardLikesRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardLikesService {

    private final BoardRepository boardRepository;
    private final BoardLikesRepository boardLikesRepository;


    // 게시글 좋아요 조회 = 필요없는 기능일수도!!
    public BoardLikesResponseDto getBoardLikes(Long boardId, AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        return new BoardLikesResponseDto(boardId,board.getBoardLikesCount());
    }

    // 게시글 좋아요 등록
    @Transactional
    public BoardLikesResponseDto postBoardLikes(Long boardId,AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        // 좋아요를 이미 누른 게시글인지 확인
        if (boardLikesRepository.existsByBoardIdandUserId(boardId,authUser.getUserId())){
            throw new AlreadyLikedException();
        }

        // 좋아요 등록
        BoardLikes boardLikes = new BoardLikes(board, authUser.getUserId());
        boardLikesRepository.save(boardLikes);

        // 게시글에 등록된 좋아요 수 증가
        board.incrementLikesCount();
        boardRepository.save(board);

        return new BoardLikesResponseDto(boardId,board.getBoardLikesCount());
    }

    // 좋아요 취소
    @Transactional
    public void deleteBoardLikes(Long boardId, Long likeId, AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        // 게시글에 해당 유저의 좋아요가 존재하는지 확인
        if (!boardLikesRepository.existsByBoardIdandUserId(boardId,authUser.getUserId())){
            throw new RuntimeException("게시글에 해당 유저의 좋아요가 존재하지 않습니다.");
        }

        // 좋아요가 존재하는지 확인
        BoardLikes boardLikes = boardLikesRepository.findById(likeId)
                .orElseThrow(()-> new NotFoundBoardLikesException());

        // 좋아요 한 유저가 맞는지 확인
        if(!boardLikes.getUserId().equals(authUser.getUserId())){
            throw new LikeOwnerMismatchException();
        }

        // 좋아요 취소
        boardLikesRepository.delete(boardLikes);
        board.decrementLikesCount();
    }
}
