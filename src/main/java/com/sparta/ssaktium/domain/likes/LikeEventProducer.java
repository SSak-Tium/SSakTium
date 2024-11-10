package com.sparta.ssaktium.domain.likes;

import com.sparta.ssaktium.domain.likes.boardLikes.BoardLikeEvent;
import com.sparta.ssaktium.domain.likes.commentLikes.CommentLikeEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

// 좋아요 카프카 생산자
@Component
public class LikeEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public LikeEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 게시글 좋아요 이벤트 전송
    public void sendBoardLikeEvent(String userId, String boardId, String eventType) {
        BoardLikeEvent boardLikeEvent = new BoardLikeEvent(userId, boardId, eventType);
        kafkaTemplate.send("board-like-events", boardLikeEvent);
    }

    // 댓글 좋아요 이벤트 전송
    public void sendCommentLikeEvent(String userId, String commentId,String eventType) {
        CommentLikeEvent commentLikeEvent = new CommentLikeEvent(userId, commentId, eventType);
        kafkaTemplate.send("comment-like-events", commentLikeEvent);  // 토픽에 전송
    }

    // 둘 다 합쳐서 하나로 전송하는 방법 (확정성)
    public void sendLikeEvent(Object likeEvent) {
        String topic = likeEvent instanceof BoardLikeEvent ? "board-like-events" : "comment-like-events";
        kafkaTemplate.send(topic, likeEvent);
    }
}