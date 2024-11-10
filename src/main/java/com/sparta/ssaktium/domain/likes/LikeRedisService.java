package com.sparta.ssaktium.domain.likes;

import lombok.Getter;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Getter
@Component
public class LikeRedisService {

    private final Jedis jedis;

    public static final String TARGET_TYPE_BOARD = "board";
    public static final String TARGET_TYPE_COMMENT = "comment";

    public LikeRedisService() {
        this.jedis = new Jedis("localhost", 6379);
    }

    // 좋아요 수 증가
    public void incrementLike(String targetType, String targetId, String userId) {
        String likeKey = "likes:" + targetType + ":" + targetId;
        String userLikeKey = targetType + "_likes:" + targetId;

        jedis.incr(likeKey);  // 좋아요 수 증가
        jedis.sadd(userLikeKey, userId);  // 사용자 좋아요 기록 추가
    }

    // 좋아요 수 감소
    public void decrementLike(String targetType, String targetId, String userId) {
        String likeKey = "likes:" + targetType + ":" + targetId;
        String userLikeKey = targetType + "_likes:" + targetId;

        jedis.decr(likeKey);  // 좋아요 수 감소
        jedis.srem(userLikeKey, userId);  // 사용자 좋아요 기록 제거
    }

    // 중복 좋아요 확인
    public boolean isLiked(String targetType, String targetId, String userId) {
        String userLikeKey = targetType + "_likes:" + targetId;
        return jedis.sismember(userLikeKey, userId);
    }

    // 좋아요 수 조회
    public int getRedisLikeCount(String targetType, String targetId) {
        String likeKey = "likes:" + targetType + ":" + targetId;
        String count = jedis.get(likeKey);
        return count != null ? Integer.parseInt(count) : 0;
    }
}
