package com.sparta.ssaktium.domain.comments.service;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.service.BoardService;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.CommentOwnerMismatchException;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BoardService boardService;

    // 댓글 조회
    public Page<CommentResponseDto> getComments(Long boardId, Long userId, int page, int size) {

        // 게시글이 있는지 확인
        boardService.findBoard(boardId);

        // 페이지네이션 생성
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByBoardId(boardId, pageable);

        // 댓글 리스트를 Dto 로 반환
        return comments.map(comment -> new CommentResponseDto(comment));
    }

    // 댓글 등록
    @Transactional
    public CommentResponseDto postComment(Long boardId, Long userId, CommentRequestDto commentRequestDto) {
        // 댓글을 작성할 게시글이 있는지 확인
        Board board = boardService.findBoard(boardId);

        // 댓글을 작성할 유저
        User user = userService.findUser(userId);

        // 댓글을 생성 후 저장
        Comment comment = new Comment(commentRequestDto.getContent(), board, user);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, Long userId, CommentRequestDto commentRequestDto) {
        // 댓글을 수정할 게시글이 있는지 확인
        boardService.findBoard(boardId);

        // 수정할 댓글이 있는지 확인
        Comment comment = findComment(commentId);

        // 해당 댓글 작성자인지 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentOwnerMismatchException();
        }

        // 댓글 내용 수정 후 저장
        comment.updateComment(commentRequestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, Long userId) {
        // 댓글 삭제할 게시글이 있는지 확인
        boardService.findBoard(boardId);

        // 삭제할 댓글이 있는지 확인
        Comment comment = findComment(commentId);

        // 해당 게시글 또는 해당 댓글 작성자인지 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new CommentOwnerMismatchException();
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 아이디로 댓글 찾기 메서드
    public Comment findComment(long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
    }

}
