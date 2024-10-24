package com.sparta.ssaktium.domain.comments.service;

import com.sparta.ssaktium.domain.boards.entity.Boards;
import com.sparta.ssaktium.domain.boards.repository.BoardsRepository;
import com.sparta.ssaktium.domain.comments.dto.request.CommentRequestDto;
import com.sparta.ssaktium.domain.comments.dto.response.CommentResponseDto;
import com.sparta.ssaktium.domain.comments.entity.Comments;
import com.sparta.ssaktium.domain.comments.exception.CommentsOwnerMismatchException;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentsException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.entity.Users;
import com.sparta.ssaktium.domain.users.exception.NotFoundUserException;
import com.sparta.ssaktium.domain.users.repository.UserRepository;
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
    private final BoardsRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 조회
    public Page<CommentResponseDto> getComments(Long boardId, AuthUser authUser, int page, int size){

        // 게시글이 있는지 확인
        Boards board = boardCheckByBoardId(boardId);

        // 페이징
        Pageable pageable = PageRequest.of(page, size);
        Page<Comments> comments = commentRepository.findByBoardId(boardId,pageable);

        return comments.map(comment -> new CommentResponseDto(comment));
    }

    // 댓글 등록
    @Transactional
    public CommentResponseDto postComment(Long boardId,AuthUser authUser, CommentRequestDto commentRequestDto) {
        // 댓글 작성할 게시글이 있는지 확인
        Boards board = boardCheckByBoardId(boardId);

        // 댓글 작성할 유저
        Users user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()->new NotFoundUserException());

        // 댓글 생성 후 저장
        Comments newcomment = new Comments(commentRequestDto.getContent(),board,user);
        commentRepository.save(newcomment);

        return new CommentResponseDto(newcomment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, AuthUser authUser, CommentRequestDto commentRequestDto) {
        // 댓글 수정할 게시글이 있는지 확인
        Boards board = boardCheckByBoardId(boardId);

        // 댓글 수정할 유저
        Users user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()->new NotFoundUserException());

        // 수정할 댓글이 있는지 확인
        Comments comments = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundCommentsException());

        // 해당 댓글 작성자인지 확인
        if (!comments.getUser().equals(user)){
            throw new CommentsOwnerMismatchException();
        }

        // 댓글 내용 수정 후 저장
        comments.updateComment(commentRequestDto.getContent());
        commentRepository.save(comments);

        return new CommentResponseDto(comments);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, AuthUser authUser) {
        // 댓글 삭제할 게시글이 있는지 확인
        Boards board = boardCheckByBoardId(boardId);

        // 댓글 삭제할 유저
        Users user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()->new NotFoundUserException());

        // 삭제할 댓글이 있는지 확인
        Comments comments = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundCommentsException());

        // 해당 게시글 또는 해당 댓글 작성자인지 확인
        if (!comments.getUser().equals(user.getId())){
            throw new CommentsOwnerMismatchException();
        }

        // 댓글 삭제
        commentRepository.delete(comments);
    }

    // 게시글이 있는지 확인
    public Boards boardCheckByBoardId (Long boardId){
        Boards board = boardRepository.findById(boardId)
                .orElseThrow(()->new RuntimeException("익센션 설정 전"));
        return board;
    }
}
