package com.sparta.springusersetting.domain.likes.boardLikes.service;

import com.sparta.springusersetting.domain.boards.entity.Board;
import com.sparta.springusersetting.domain.boards.repository.BoardRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.likes.boardLikes.dto.BoardLikesResponseDto;
import com.sparta.springusersetting.domain.likes.boardLikes.entity.BoardLikes;
import com.sparta.springusersetting.domain.likes.boardLikes.repository.BoardLikesRepository;
import com.sparta.springusersetting.domain.user.entity.User;
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


    // 게시글 좋아요 조회
    public BoardLikesResponseDto getBoardLikes(Long boardId, AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        // 게시글에 좋아요가 몇개인지 확인
        int boardLikesCount = boardLikesRepository.countByBoardId(boardId);

        return new BoardLikesResponseDto(boardId,boardLikesCount);
    }

    // 게시글 좋아요 등록
    @Transactional
    public BoardLikesResponseDto postBoardLikes(Long boardId,AuthUser authUser) {
        // 게시글이 있는지 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익셉션 설정 전"));

        // 좋아요를 이미 누른 게시글인지 확인
        if (boardLikesRepository.existsByBoardIdandUserId(boardId,authUser.getUserId())){
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        // 좋아요 등록
        BoardLikes boardLikes = new BoardLikes(board, authUser.getUserId());
        boardLikesRepository.save(boardLikes);
        // 게시글에 나오는 좋아요 수 증가
        board.plusLikesCount();
        boardRepository.save(board);

        return new BoardLikesResponseDto(boardId,board.getBoardLikesCount());
    }
}
