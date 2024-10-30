package com.sparta.ssaktium.domain.comment;


import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.exception.NotFoundBoardException;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.CommentOwnerMismatchException;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.comments.service.CommentService;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.enums.UserRole;
import com.sparta.ssaktium.domain.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// 댓글 서비스 테스트
@ExtendWith(MockitoExtension.class) // Mockito 사용 설정
class CommentServiceTest {

    // 실제 테스트할 서비스 객체
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private UserService userService;

    @Test
    public void 댓글_조회_성공() {
        // given 0번 부터 조회할 예정이기 때문에 page 0
        Long boardId = 1L;
        Pageable pageable = PageRequest.of(0, 10); // 페이지 설정

        Board board = new Board();
        Comment comment1 = new Comment("첫 번째 댓글", board, new User("user@example.com", "password", "username", UserRole.ROLE_USER));
        Comment comment2 = new Comment("두 번째 댓글", board, new User("user@example.com", "password", "username", UserRole.ROLE_USER));
        Page<Comment> commentPage = new PageImpl<>(List.of(comment1, comment2), pageable, 2); // 두 개의 댓글을 가진 페이지

        // Mock 설정
        when(boardService.getBoardById(boardId)).thenReturn(board);
        when(commentRepository.findByBoard(board, pageable)).thenReturn(commentPage);

        // when 테스트 시 getComments 의 page -1 을 고려해서 page 에 1 넣음
        Page<CommentResponseDto> responseDtos = commentService.getComments(boardId, 1, 10);

        // then
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.getTotalElements()); // 총 댓글 수가 일치해야 함
    }

    @Test
    void 댓글_조회_실패_게시글_존재하지_않음() {
        // given
        Long boardId = 1L; // 조회할 게시글 ID
        int page = 1;      // 요청한 페이지
        int size = 10;     // 페이지 크기

        // 게시글이 없을 때 예외 처리 발생 해야함
        when(boardService.getBoardById(boardId))
                .thenThrow(new NotFoundBoardException());

        // when & then: 예외가 발생하는지 확인
        assertThrows(NotFoundBoardException.class, () -> {
            commentService.getComments(boardId, page, size);
        });
    }


    @Test
    public void 댓글_등록_성공() {
        // given
        Long userId = 1L;
        Long boardId = 1L; // 게시글 ID
        String content = "댓글 내용";
        CommentRequestDto requestDto = new CommentRequestDto(content); // 댓글 요청 DTO
        User user = new User("user@example.com", "password", "username", UserRole.ROLE_USER); // 사용자 객체
        Board board = new Board(); // 게시글 객체

        // 게시글 및 사용자 반환 설정
        when(boardService.getBoardById(boardId)).thenReturn(board); // 게시글이 존재함
        when(userService.findUser(userId)).thenReturn(user); // 사용자 존재함
        when(commentRepository.save(ArgumentMatchers.any(Comment.class))).
                thenAnswer(invocation -> invocation.getArgument(0)); // 댓글 저장 시 입력된 댓글 객체 반환

        // when
        CommentResponseDto responseDto = commentService.postComment(boardId, userId, requestDto);

        // then
        assertNotNull(responseDto); // 응답 DTO가 null이 아님
        assertEquals("댓글 내용", responseDto.getContent()); // 댓글 내용이 요청한 내용과 일치함
    }

    @Test
    public void 댓글_등록_실패_게시글_존재하지_않음() {
        // given
        Long userId = 1L;
        Long boardId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");

        // Mock 설정: 게시글이 존재하지 않는 경우
        when(boardService.getBoardById(boardId)).thenThrow(new NotFoundBoardException());

        // when
        assertThrows(NotFoundBoardException.class, () ->
                commentService.postComment(boardId, userId, requestDto));
    }

    //     댓글 수정 성공 테스트
    @Test
    public void 댓글_수정_성공() throws Exception {
        // given
        Long userId = 1L;
        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID
        String newContent = "수정된 댓글 내용"; // 수정할 댓글 내용
        CommentRequestDto requestDto = new CommentRequestDto(newContent); // 수정 요청 DTO 생성

        // Mock 설정: 게시글이 존재하는 경우
        Board board = new Board(); // 게시글 객체 생성
        when(boardService.getBoardById(boardId)).thenReturn(board); // 게시글 존재 설정

        // Mock 설정: 리플렉션으로 유저 Id 설정
        User user = new User();
        setField(user, "id", userId);

        // Mock 설정: 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, user); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when
        CommentResponseDto responseDto = commentService.updateComment(userId, boardId, commentId, requestDto); // 댓글 수정 메서드 호출

        // then
        assertNotNull(responseDto); // 응답 DTO가 null이 아님을 확인
        assertEquals(newContent, responseDto.getContent()); // 수정된 댓글 내용이 일치함을 확인
    }

    @Test
    public void 댓글_수정_실패_댓글_존재하지_않음() {
        // given
        Long userId = 1L;
        Long boardId = 1L;
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto("수정된 댓글 내용");

        // Mock 설정: 게시글은 존재하는데 댓글은 존재하지 않는 경우
        Board board = new Board();
        when(boardService.getBoardById(boardId)).thenReturn(board);
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundCommentException.class, () ->
                commentService.updateComment(userId, boardId, commentId, requestDto));
    }

    // 댓글 수정 실패 테스트
    @Test
    public void 댓글_수정_실패_작성자_아님() throws Exception {
        // given
        Long userId = 1L;
        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID
        String newContent = "수정된 댓글 내용"; // 수정할 댓글 내용
        CommentRequestDto requestDto = new CommentRequestDto(newContent); // 수정 요청 DTO 생성

        // Mock 설정: 게시글이 존재하는 경우
        Board board = new Board(); // 게시글 객체 생성
        when(boardService.getBoardById(boardId)).thenReturn(board); // 게시글 존재 설정

        // Mock 설정: 리플렉션으로 유저 Id 설정
        User user = new User();
        setField(user, "id", userId);

        // Mock 설정: 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, user); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when & then
        assertThrows(CommentOwnerMismatchException.class, () -> {
            commentService.updateComment(2L, boardId, commentId, requestDto); // 댓글 수정 메서드 호출
        });
    }

    @Test
    public void 댓글_삭제_성공() throws Exception {
        // given
        Long userId = 1L;
        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID

        // Mock 설정: 리플렉션으로 유저 Id 설정
        User user = new User();
        setField(user, "id", userId);

        // Mock 설정: 댓글을 생성하기 위한 게시글
        Board board = new Board(); // 게시글 객체 생성
        setField(board, "user", user);

        // Mock 설정: 삭제할 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, user); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        // when
        commentService.deleteComment(userId, commentId); // 댓글 삭제 메서드 호출

        // then
        verify(commentRepository).delete(existingComment); // 댓글 삭제가 호출되었는지 검증
    }

    @Test
    public void 댓글_삭제_실패_댓글_존재하지_않음() {
        // given
        Long userId = 1L;
        Long commentId = 1L;

        // Mock 설정:  댓글이 존재하지 않는 경우
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundCommentException.class, () ->
                commentService.deleteComment(userId, commentId));
    }

    @Test
    public void 댓글_삭제_실패_댓글_게시글_작성자_아님() throws Exception {
        // given
        Long userId = 1L;
        Long boardId = 1L; // 게시글 ID
        Long commentId = 1L; // 댓글 ID

        // Mock 설정: 리플렉션으로 유저 Id 설정
        User BoardOwner = new User();
        setField(BoardOwner, "id", userId);
        User commentOwner = new User();
        setField(commentOwner, "id", 2L);

        // Mock 설정: 댓글을 생성하기 위한 게시글
        Board board = new Board(); // 게시글 객체 생성
        setField(board, "user", BoardOwner);

        // Mock 설정: 삭제할 댓글이 존재하는 경우
        Comment existingComment = new Comment("댓글 내용", board, commentOwner); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment)); // 댓글 존재 설정

        assertThrows(CommentOwnerMismatchException.class, () ->
                commentService.deleteComment(3L, commentId));
    }

    // 찾기 메서드 테스트 성공
    @Test
    public void findComment_성공() throws Exception{
        // given
        Long commentId =1L;

        // Mock 설정: 찾아올 댓글이 존재하는 경우
        Comment existingComment = new Comment(); // 기존 댓글 객체 생성
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // when
        Comment foundComment = commentService.findComment(commentId);

        // then
        assertEquals(existingComment, foundComment);
    }

    // 찾기 메서드 테스트 실패
    @Test
    public void findComment_실패(){
        // given
        Long commentId =1L;

        // when
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundCommentException.class,()->
                commentService.findComment(commentId));
    }

    // 리플렉션을 통해 필드 값을 설정하는 메서드
    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}



