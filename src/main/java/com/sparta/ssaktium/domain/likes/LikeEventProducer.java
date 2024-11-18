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

    // 좋아요 이벤트 전송 (확장 가능)
    public void sendLikeEvent(LikeEvent likeEvent) {
        String topic = getTopicForEvent(likeEvent);

        try {
            kafkaTemplate.send(topic, likeEvent); // 비동기 전송 + 완료 확인
        } catch (Exception e){
            System.err.println("Kafka 메시지 전송 실패 :" + e.getMessage());
        }
    }

    // 이벤트 타입에 따라 토픽 결정
    private String getTopicForEvent(LikeEvent likeEvent) {
        if (likeEvent instanceof BoardLikeEvent) {
            return "board-like-events";
        } else if (likeEvent instanceof CommentLikeEvent) {
            return "comment-like-events";
        } else {
            throw new IllegalArgumentException(
                    "좋아요 기능을 제공하지 않는 이벤트입니다." + likeEvent.getClass().getSimpleName());
        }
    }
}