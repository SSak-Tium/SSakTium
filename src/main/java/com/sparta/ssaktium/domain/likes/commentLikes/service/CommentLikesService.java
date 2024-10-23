package com.sparta.ssaktium.domain.likes.commentLikes.service;

import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikesReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLikes;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikesRepository;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.LikeOwnerMismatchException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikesException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundCommentLikesException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikesService {

    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;

    // 댓글에 좋아요 등록
    @Transactional
    public CommentLikesReponseDto postCommentLike(Long commentId, AuthUser authUser) {
        // 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundCommentException());

        // 좋아요를 이미 누른 댓글인지 확인
        if(commentLikesRepository.existsByCommentIdandUserId(commentId,authUser.getUserId())){
            throw new AlreadyLikedException();
        }

        // 좋아요 등록
        CommentLikes commentLikes = new CommentLikes(comment,authUser.getUserId());
        commentLikesRepository.save(commentLikes);

        // 댓글에 등록된 좋아요 수 증가
        comment.incrementLikesCount();
        commentRepository.save(comment);

        return new CommentLikesReponseDto(commentId,comment.getCommentLikesCount());
    }

    // 댓글에 좋아요 취소
    @Transactional
    public void deleteCommentLike(Long commentId, Long likeId, AuthUser authUser) {
        // 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundCommentException());

        // 댓글에 해당 유저의 좋아요가 있는지 확인
        if(!commentLikesRepository.existsByCommentIdandUserId(commentId,authUser.getUserId())){
            throw new NotFoundCommentLikesException(); // 수정 필요
        }

        // 좋아요가 있는지 확인
        CommentLikes commentLikes = commentLikesRepository.findById(likeId)
                .orElseThrow(()-> new NotFoundCommentLikesException());

        // 좋아요 한 유저가 맞는지 확인
        if(!commentLikes.getUserId().equals(authUser.getUserId())){
            throw new LikeOwnerMismatchException();
        }

        // 좋아요 취소
        commentLikesRepository.delete(commentLikes);
        comment.decrementLikesCount();

    }
}
