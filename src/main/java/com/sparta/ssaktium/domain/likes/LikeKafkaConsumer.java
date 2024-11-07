package com.sparta.ssaktium.domain.likes;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// 좋아요 카프카 소비자
@Component
public class LikeKafkaConsumer {

    private final LikeRedisService redisService;

    public LikeKafkaConsumer(LikeRedisService redisService) {
        this.redisService = redisService;
    }

    @KafkaListener(topics = "likes_events", groupId = "like-consumer-group")
    public void consumeLikeEvent(LikeEvent event) {
        String postId = event.getBoardId();
        String userId = event.getUserId();

        // Redis에 중복 체크 후 좋아요 반영
        if (!redisService.isLiked(postId, userId)) {
            redisService.incrementLike(postId, userId);
        }
    }
}