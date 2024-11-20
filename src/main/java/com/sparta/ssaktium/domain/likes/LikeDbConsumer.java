package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.likes.boardLikes.BoardLikeEvent;
import com.sparta.ssaktium.domain.likes.commentLikes.CommentLikeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeDbConsumer {

    private final LikeDbService likeDbService;

    public LikeDbConsumer(LikeDbService likeDbService) {
        this.likeDbService = likeDbService;
    }

    @KafkaListener(topics = "board-like-events", groupId = "like-db-group")
    public void handleBoardLikeEvent(BoardLikeEvent event) {
        try {
            String eventType = event.getEventType();
            String boardId = event.getBoardId();
            String userId = event.getUserId();

            switch (eventType) {
                case "LIKE":
                    likeDbService.saveLike("board", boardId, userId); // DB에 저장
                    log.info("like-db 게시글 좋아요 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, boardId, userId);
                    break;
                case "CANCEL":
                    likeDbService.deleteLike("board", boardId, userId); // DB에서 삭제
                    log.info("like-db 게시글 좋아요 취소 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, boardId, userId);
                    break;
                default:
                    log.warn("like-db 알 수 없는 게시글 이벤트 타입입니다.: {}", eventType);
            }
        } catch (Exception e) {
            log.error("like-db 게시글이벤트 처리 중 오류 발생 : {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "comment-like-events", groupId = "like-db-group")
    public void handleCommentLikeEvent(CommentLikeEvent event) {
        try {
            String eventType = event.getEventType();
            String commentId = event.getCommentId();
            String userId = event.getUserId();

            switch (eventType) {
                case "LIKE":
                    likeDbService.saveLike("comment", commentId, userId); // DB에 저장
                    log.info("like-db 댓글 좋아요 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, commentId, userId);
                    break;
                case "CANCEL":
                    likeDbService.deleteLike("comment", commentId, userId); // DB에서 삭제
                    log.info("like-db 댓글 좋아요 취소 처리 완료: eventType={}, boardId={}, userId={}",
                            eventType, commentId, userId);
                    break;
                default:
                    log.warn("like-db 알 수 없는 댓글 이벤트 타입입니다.: {}", eventType);
                    break;
            }
        } catch (Exception e) {
            log.error("like-db 댓글이벤트 처리 중 오류 발생 : {}", e.getMessage(), e);
        }
    }
}

