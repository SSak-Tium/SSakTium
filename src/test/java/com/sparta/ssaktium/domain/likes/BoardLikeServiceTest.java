package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.likes.boardLikes.dto.BoardLikeResponseDto;
import com.sparta.ssaktium.domain.likes.boardLikes.entity.BoardLike;
import com.sparta.ssaktium.domain.likes.boardLikes.repository.BoardLikeRepository;
import com.sparta.ssaktium.domain.likes.boardLikes.service.BoardLikeService;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
public class BoardLikeServiceTest {

    // 실제 테스트할 서비스 객체
    @InjectMocks
    private BoardLikeService boardLikeService;

    @Mock
    private BoardLikeRepository boardLikeRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    public void 게시글_좋아요_등록_성공() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        // Mock 설정: 게시글은 있고, 아직 좋아요 안누른 경우
        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardLikeRepository.existsByBoardIdAndUserId(boardId, userId)).thenReturn(false);

        // when 좋아요 등록
        BoardLikeResponseDto response = boardLikeService.postBoardLikes(boardId, userId);

        // then
        assertNotNull(response); // 응답이 null이 아님
        assertEquals(boardId, response.getBoardId()); // 게시글 ID 검증
        assertEquals(1, response.getBoardLikesCount()); // 좋아요 수 증가 검증
    }

    @Test
    void 게시글_좋아요_등록_실패_게시글이_존재하지_않음() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        // Mock 설정
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundBoardException.class, () -> boardLikeService.postBoardLikes(userId, boardId));
    }

    @Test
    public void 게시글_좋아요_등록_실패_이미_좋아요한_경우() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        // Mock 설정: 해당 게시글에 이미 좋아요 누른 경우
        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardLikeRepository.existsByBoardIdAndUserId(boardId, userId)).thenReturn(true);

        // when & then: 예외 발생 검증
        assertThrows(AlreadyLikedException.class, () -> boardLikeService.postBoardLikes(boardId, userId));
    }

    @Test
    public void 게시글_좋아요_취소_성공() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        // Mock 설정: 해당 게시글에 이미 좋아요 누른경우 + 좋아요 수는 1
        Board board = new Board();
        BoardLike boardLike = new BoardLike(board, userId);
        board.incrementLikesCount();

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardLikeRepository.findByBoardIdAndUserId(boardId, userId)).thenReturn(Optional.of(boardLike)); // 좋아요 객체 반환

        // when: 좋아요 취소
        boardLikeService.deleteBoardLikes(userId, boardId);

        // then: 좋아요 수가 감소했는지 확인
        assertEquals(0, board.getBoardLikesCount()); // 좋아요 수 감소 검증
    }

    @Test
    public void 게시글_좋아요_취소_실패_게시글이_존재하지_않음() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        // Mock 설정: 해당 게시글이 존재하지 않는 경우
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when & then: 예외 발생 검증
        assertThrows(NotFoundBoardException.class, () ->
                boardLikeService.deleteBoardLikes(userId, boardId));
    }

    @Test
    public void 게시글_좋아요_취소_실패_좋아요가_존재하지_않음() {
        // given
        Long userId = 1L;
        Long boardId = 1L;

        // Mock 설정: 게시글은 있는데 좋아요가 없는 경우
        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when & then: 예외 발생 검증
        assertThrows(NotFoundBoardLikeException.class, () -> boardLikeService.deleteBoardLikes(userId, boardId));
    }
}
