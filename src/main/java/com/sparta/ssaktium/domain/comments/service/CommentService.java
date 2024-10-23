package com.sparta.ssaktium.domain.comments.service;

import com.sparta.ssaktium.domain.boards.entity.Board;
import com.sparta.ssaktium.domain.boards.repository.BoardRepository;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.CommentOwnerMismatchException;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.user.entity.User;
import com.sparta.ssaktium.domain.user.exception.NotFoundUserException;
import com.sparta.ssaktium.domain.user.repository.UserRepository;
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
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 조회
    public Page<CommentResponseDto> getComments(Long boardId, AuthUser authUser, int page, int size){

        // 게시글이 있는지 확인
        Board board = boardCheckByBoardId(boardId);

        // 페이징
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findByBoardId(boardId,pageable);

        return comments.map(comment -> new CommentResponseDto(comment));
    }

    // 댓글 등록
    @Transactional
    public CommentResponseDto postComment(Long boardId,AuthUser authUser, CommentRequestDto commentRequestDto) {
        // 댓글 작성할 게시글이 있는지 확인
        Board board = boardCheckByBoardId(boardId);

        // 댓글 작성할 유저
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()->new NotFoundUserException());

        // 댓글 생성 후 저장
        Comment newcomment = new Comment(commentRequestDto.getContent(),board,user);
        commentRepository.save(newcomment);

        return new CommentResponseDto(newcomment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, AuthUser authUser, CommentRequestDto commentRequestDto) {
        // 댓글 수정할 게시글이 있는지 확인
        Board board = boardCheckByBoardId(boardId);

        // 댓글 수정할 유저
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()->new NotFoundUserException());

        // 수정할 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundCommentException());

        // 해당 댓글 작성자인지 확인
        if (!comment.getUser().equals(user.getId())){
            throw new CommentOwnerMismatchException();
        }

        // 댓글 내용 수정 후 저장
        comment.updateComment(commentRequestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, AuthUser authUser) {
        // 댓글 삭제할 게시글이 있는지 확인
        Board board = boardCheckByBoardId(boardId);

        // 댓글 삭제할 유저
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()->new NotFoundUserException());

        // 삭제할 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundCommentException());

        // 해당 게시글 또는 해당 댓글 작성자인지 확인
        if (!comment.getUser().equals(user.getId())){
            throw new CommentOwnerMismatchException();
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }

    // 게시글이 있는지 확인
    public Board boardCheckByBoardId (Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익센션 설정 전"));
        return board;
    }
}
