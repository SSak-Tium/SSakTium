package com.sparta.ssaktium.domain.likes.commentLikes.service;

import com.sparta.ssaktium.domain.comments.exception.NotFoundCommentException;
import com.sparta.ssaktium.domain.comments.repository.CommentRepository;
import com.sparta.ssaktium.domain.likes.LikeEventProducer;
import com.sparta.ssaktium.domain.likes.LikeRedisService;
import com.sparta.ssaktium.domain.likes.boardLikes.BoardLikeEvent;
import com.sparta.ssaktium.domain.likes.commentLikes.CommentLikeEvent;
import com.sparta.ssaktium.domain.likes.commentLikes.dto.CommentLikeReponseDto;
import com.sparta.ssaktium.domain.likes.commentLikes.repository.CommentLikeRepository;
import com.sparta.ssaktium.domain.likes.exception.AlreadyLikedException;
import com.sparta.ssaktium.domain.likes.exception.NotFoundBoardLikeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final LikeEventProducer likeProducer; // 카프카 이벤트 프로듀서 주입
    private final LikeRedisService likeRedisService; // 레디스 좋아요 수 조회용

    @Value("${db-lock-enabled:true}")
    private boolean dbLockEnabled;

    // 댓글에 좋아요 등록
    @Transactional
    public CommentLikeReponseDto postCommentLike(Long userId, Long commentId) {
        // 댓글이 있는지 확인
        commentRepository.findById(commentId).
                orElseThrow(() -> new NotFoundCommentException());

        // 좋아요를 이미 누른 댓글인지 확인
        if (likeRedisService.isLiked(
                LikeRedisService.TARGET_TYPE_COMMENT,
                commentId.toString(),
                userId.toString())) {
            throw new AlreadyLikedException();
        }

        // 좋아요 등록(Kafka -> Redis)
        likeProducer.sendLikeEvent(new CommentLikeEvent(
                userId.toString(), commentId.toString(), "LIKE"));

        // 좋아요 수 레디스에서 반영
        int redisLikeCount = likeRedisService.getRedisLikeCount(
                LikeRedisService.TARGET_TYPE_COMMENT, commentId.toString());

        return new CommentLikeReponseDto(commentId, redisLikeCount);
    }

    // 댓글에 좋아요 취소
    @Transactional
    public void deleteCommentLike(Long userId, Long commentId) {
        // 댓글이 있는지 확인
        commentRepository.findById(commentId).
                orElseThrow(() -> new NotFoundCommentException());

        // 댓글에 해당 유저의 좋아요가 있는지 확인
        if (likeRedisService.isLiked(
                LikeRedisService.TARGET_TYPE_COMMENT, commentId.toString(), userId.toString())) {
            throw new NotFoundBoardLikeException();
        }

        // 카프카 좋아요 취소 이벤트
        likeProducer.sendLikeEvent(new CommentLikeEvent(
                userId.toString(), commentId.toString(), "CANCEL"));
    }
}
