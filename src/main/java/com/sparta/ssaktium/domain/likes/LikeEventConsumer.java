package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.likes.boardLikes.BoardLikeEvent;
import com.sparta.ssaktium.domain.likes.commentLikes.CommentLikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeEventConsumer {

    private final LikeRedisService likeRedisService;

    public LikeEventConsumer(LikeRedisService likeRedisService) {
        this.likeRedisService = likeRedisService;
    }

    @KafkaListener(topics = "board-like-events", groupId = "like-redis-group")
    public void handleBoardLikeEvent(BoardLikeEvent event) {
        try {
            String eventType = event.getEventType();
            String boardId = event.getBoardId();
            String userId = event.getUserId();

            switch (eventType) {
                case "LIKE":
                    likeRedisService.incrementLike(
                            LikeRedisService.TARGET_TYPE_BOARD,
                            boardId,
                            userId); // 게시글 좋아요 추가
                    log.info("like-redis 게시글 좋아요 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, boardId, userId);
                    break;
                case "CANCEL":
                    likeRedisService.decrementLike(
                            LikeRedisService.TARGET_TYPE_BOARD,
                            boardId,
                            userId); // 게시글 좋아요 취소
                    log.info("like-redis 게시글 좋아요 취소 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, boardId, userId);
                    break;
                default:
                    log.warn("like-redis 알 수 없는 게시글 이벤트 타입입니다.: {}", eventType);
            }
        } catch (Exception e){
            log.error("like-redis 게시글이벤트 처리 중 오류 발생 : {}",e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "comment-like-events", groupId = "like-redis-group")
    public void handleCommentLikeEvent(CommentLikeEvent event) {
        try {
            String eventType = event.getEventType();
            String commentId = event.getCommentId();
            String userId = event.getUserId();

            switch (eventType) {
                case "LIKE":
                    likeRedisService.incrementLike(
                            LikeRedisService.TARGET_TYPE_COMMENT,
                            commentId,
                            userId); // 댓글 좋아요 추가
                    log.info("like-db 댓글 좋아요 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, commentId, userId);
                    break;
                case "CANCEL":
                    likeRedisService.decrementLike(
                            LikeRedisService.TARGET_TYPE_COMMENT,
                            commentId,
                            userId); // 댓글 좋아요 취소
                    log.info("like-db 댓글 좋아요 취소 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, commentId, userId);
                    break;
                default:
                    log.warn("like-redis 알 수 없는 댓글 이벤트 타입입니다.: {}", eventType);
                    break;
            }
        } catch (Exception e){
            log.error("like-redis 댓글이벤트 처리 중 오류 발생 : {}",e.getMessage(), e);
        }
    }
}