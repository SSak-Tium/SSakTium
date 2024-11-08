package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.likes.boardLikes.BoardLikeEvent;
import com.sparta.ssaktium.domain.likes.commentLikes.CommentLikeEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LikeEventConsumer {

    private final LikeRedisService likeRedisService;

    public LikeEventConsumer(LikeRedisService likeRedisService) {
        this.likeRedisService = likeRedisService;
    }

    public static final String TARGET_TYPE_BOARD = "Board";
    public static final String TARGET_TYPE_COMMENT = "Comment";

    @KafkaListener(topics = "board-like-events", groupId = "like-group")
    public void handleBoardLikeEvent(BoardLikeEvent event) {
        String eventType = event.getEventType();
        String boardId = event.getBoardId();
        String userId = event.getUserId();

        switch (eventType) {
            case "LIKE":
                likeRedisService.incrementLike(TARGET_TYPE_BOARD,boardId, userId); // 게시글 좋아요 추가
                break;
            case "CANCEL":
                likeRedisService.decrementLike(TARGET_TYPE_BOARD,boardId, userId); // 게시글 좋아요 취소
                break;
            default:
                System.out.println("알 수 없는 게시글 이벤트 타입입니다.: " + eventType);
                break;
        }
    }

    @KafkaListener(topics = "comment-like-events", groupId = "like-group")
    public void handleCommentLikeEvent(CommentLikeEvent event) {
        String eventType = event.getEventType();
        String commentId = event.getCommentId();
        String userId = event.getUserId();

        switch (eventType) {
            case "LIKE":
                likeRedisService.incrementLike(TARGET_TYPE_COMMENT,commentId, userId); // 댓글 좋아요 추가
                break;
            case "CANCEL":
                likeRedisService.decrementLike(TARGET_TYPE_COMMENT,commentId, userId); // 댓글 좋아요 취소
                break;
            default:
                System.out.println("알 수 없는 댓글 이벤트 타입입니다.: " + eventType);
                break;
        }
    }
}