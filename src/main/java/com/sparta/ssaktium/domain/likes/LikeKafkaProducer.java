package com.sparta.ssaktium.domain.likes;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

// 좋아요 카프카 생산자
@Component
public class LikeKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public LikeKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLikeEvent(String userId, String postId) {
        LikeEvent event = new LikeEvent(userId, postId, "like");
        kafkaTemplate.send("likes_events", event);  // Kafka 토픽에 이벤트 전송
    }
}