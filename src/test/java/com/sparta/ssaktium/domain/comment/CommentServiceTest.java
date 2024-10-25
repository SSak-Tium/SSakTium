package com.sparta.ssaktium.domain.comment;


import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.CommentOwnerMismatchException;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// 댓글 서비스 테스트
@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
class CommentServiceTest {

    // 실제 테스트할 서비스 객체
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void 댓글_조회_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
        Long boardId = 1L;
        Pageable pageable = PageRequest.of(0, 10); // 페이지 설정
        Board board = new Board();
        Comment comment1 = new Comment("첫 번째 댓글", board, new User("user@example.com", "password", "username", UserRole.ROLE_USER));
        Comment comment2 = new Comment("두 번째 댓글", board, new User("user@example.com", "password", "username", UserRole.ROLE_USER));
        Page<Comment> commentPage = new PageImpl<>(List.of(comment1, comment2), pageable, 2); // 두 개의 댓글을 가진 페이지

        // Mock 설정
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(commentRepository.findByBoardId(boardId, pageable)).thenReturn(commentPage);

        // when
        Page<CommentResponseDto> responseDtos = commentService.getComments(boardId, authUser, 0, 10);

        // then
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.getTotalElements()); // 총 댓글 수가 일치해야 함
    }

    @Test
    public void 댓글_등록_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER); // 인증된 사용자
        Long boardId = 1L; // 게시글 ID
        String content = "댓글 내용";
        CommentRequestDto requestDto = new CommentRequestDto(content); // 댓글 요청 DTO
        User user = new User("user@example.com", "password", "username", UserRole.ROLE_USER); // 사용자 객체
        Board board = new Board(); // 게시글 객체

        // 게시글 및 사용자 반환 설정
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board)); // 게시글이 존재함
        when(userRepository.findByEmail(authUser.getEmail())).thenReturn(Optional.of(user)); // 사용자 존재함
        when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0)); // 댓글 저장 시 입력된 댓글 객체 반환

        // when
        CommentResponseDto responseDto = commentService.postComment(boardId, authUser, requestDto);

        // then
        assertNotNull(responseDto); // 응답 DTO가 null이 아님
        assertEquals("댓글 내용", responseDto.getContent()); // 댓글 내용이 요청한 내용과 일치함
    }

    @Test
    public void 댓글_등록_실패_게시글_존재하지_않음() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
        Long boardId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");

        // Mock 설정: 게시글이 존재하지 않는 경우
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundBoardException.class, () ->
                commentService.postComment(boardId, authUser, requestDto));
    }

    // 댓글 수정 성공 테스트
    @Test
    public void 댓글_수정_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER); // 인증된 사용자 생성
        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID
        String newContent = "수정된 댓글 내용"; // 수정할 댓글 내용
        CommentRequestDto requestDto = new CommentRequestDto(newContent); // 수정 요청 DTO 생성

        // Mock 설정: 게시글이 존재하는 경우
        Board board = new Board(); // 게시글 객체 생성
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board)); // 게시글 존재 설정

        // Mock 설정: authUser 로 user 생성
        User user = User.fromAuthUser(authUser);

        // Mock 설정: 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, user); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when
        CommentResponseDto responseDto = commentService.updateComment(boardId, commentId, authUser, requestDto); // 댓글 수정 메서드 호출

        // then
        assertNotNull(responseDto); // 응답 DTO가 null이 아님을 확인
        assertEquals(newContent, responseDto.getContent()); // 수정된 댓글 내용이 일치함을 확인
    }

    @Test
    public void 댓글_수정_실패_댓글_존재하지_않음() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
        Long boardId = 1L;
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("수정된 댓글 내용");

        // Mock 설정: 게시글은 존재하는데 댓글은 존재하지 않는 경우
        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundCommentException.class, () ->
                commentService.updateComment(boardId, commentId, authUser, requestDto));
    }

    // 댓글 수정 실패 테스트
    @Test
    public void 댓글_수정_실패_작성자_아님() {
        // given
        AuthUser writeAuthUser = new AuthUser(1, "user@example.com", UserRole.ROLE_USER); // 작성자
        AuthUser requestUpdateAuthUser = new AuthUser(2, "user@example.com", UserRole.ROLE_USER); // 수정 요청자

        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID
        String newContent = "수정된 댓글 내용"; // 수정할 댓글 내용
        CommentRequestDto requestDto = new CommentRequestDto(newContent); // 수정 요청 DTO 생성

        // Mock 설정: 게시글이 존재하는 경우
        Board board = new Board(); // 게시글 객체 생성
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board)); // 게시글 존재 설정

        // Mock 설정: authUser 로 user 생성(댓글 작성자)
        User user = User.fromAuthUser(writeAuthUser);

        // Mock 설정: 작성자가 아닌 사용자가 수정 시도할 때 예외 발생 확인
        Comment existingComment = new Comment("댓글 내용", board, user); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when
        assertThrows(CommentOwnerMismatchException.class, ()->
                commentService.updateComment(boardId, commentId, requestUpdateAuthUser, requestDto));

        // then
        verify(commentRepository,never()).save(ArgumentMatchers.any(Comment.class));
    }

    @Test
    public void 댓글_삭제_성공() {
        // given
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "user@example.com", UserRole.ROLE_USER); // 삭제 요청자
        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID


        // Mock 설정: 게시글이 존재하는 경우
        Board board = new Board(); // 게시글 객체 생성
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board)); // 게시글 존재 설정

        // authUser 로 user 생성(댓글 작성자)
        User user = User.fromAuthUser(authUser);

        // 리플렉션 통해서 user Id 설정. 1 , 2 둘 다 해서 1은 성공, 2는 실패 테스트
        // Mock 설정: 삭제할 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, user); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when
        commentService.deleteComment(boardId, commentId, authUser); // 댓글 삭제 메서드 호출

        // then
        verify(commentRepository).delete(existingComment); // 댓글 삭제가 호출되었는지 검증
    }

    @Test
    public void 댓글_삭제_실패_댓글_존재하지_않음() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_USER);
        Long boardId = 1L;
        Long commentId = 1L;

        // Mock 설정: 게시글은 존재하는데 댓글은 존재하지 않는 경우
        Board board = new Board();
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundCommentException.class, () ->
                commentService.deleteComment(boardId, commentId, authUser));
    }

    @Test
    public void 댓글_삭제_실패_작성자_아님() {
        // given
        AuthUser writeAuthUser = new AuthUser(1, "user@example.com", UserRole.ROLE_USER); // 작성자
        AuthUser deleteRequestAuthUser = new AuthUser(2, "user@example.com", UserRole.ROLE_USER); // 삭제 요청자

        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID


        // Mock 설정: 게시글이 존재하는 경우
        Board board = new Board(); // 게시글 객체 생성
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board)); // 게시글 존재 설정

        // authUser 로 user 생성(댓글 작성자)
        User user = User.fromAuthUser(writeAuthUser);

        // Mock 설정: 삭제할 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, user); //  댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when 일치하지않으면 예외처리 발생
        assertThrows(CommentOwnerMismatchException.class, ()->
                commentService.deleteComment(boardId, commentId, deleteRequestAuthUser));

        // then
        verify(commentRepository, never()).delete(existingComment); // 댓글 삭제가 호출되었는지 검증
    }



}



