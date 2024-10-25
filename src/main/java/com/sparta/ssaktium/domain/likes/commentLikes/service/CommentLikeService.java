package com.sparta.ssaktium.domain.likes.commentLikes.service;

import com.sparta.ssaktium.domain.comments.entity.Comment;
import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikeReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.entity.CommentLike;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikeRepository;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.LikeOwnerMismatchException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundCommentLikeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글에 좋아요 등록
    @Transactional
    public CommentLikeReponseDto postCommentLike(Long commentId, Long userId) {
        // 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException());

        // 좋아요를 이미 누른 댓글인지 확인
        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, userId)) {
            throw new AlreadyLikedException();
        }

        // 좋아요 등록
        CommentLike commentLike = new CommentLike(comment, userId);
        commentLikeRepository.save(commentLike);

        // 댓글에 등록된 좋아요 수 증가
        comment.incrementLikesCount();
        commentRepository.save(comment);

        return new CommentLikeReponseDto(commentId, comment.getCommentLikesCount());
    }

    // 댓글에 좋아요 취소
    @Transactional
    public void deleteCommentLike(Long commentId, Long userId) {
        // 댓글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException());

        // 댓글에 해당 유저의 좋아요가 있는지 확인
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId)
                .orElseThrow(() -> new NotFoundCommentLikeException());

        // 좋아요 한 유저가 맞는지 확인
        if (!commentLike.getUserId().equals(userId)) {
            throw new LikeOwnerMismatchException();
        }

        // 좋아요 취소
        commentLikeRepository.delete(commentLike);
        comment.decrementLikesCount();

    }
}
