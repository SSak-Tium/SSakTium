package com.sparta.ssaktium.domain.likes;

import lombok.Getter;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Getter
@Component
public class LikeRedisService {

    private final Jedis jedis;

    public LikeRedisService() {
        this.jedis = new Jedis("localhost", 6379);
    }

    public void incrementLike(String postId, String userId) {
        jedis.incr("likes:" + postId);  // 좋아요 수 증가
        jedis.sadd("post_likes:" + postId, userId);  // 사용자 좋아요 기록 추가
    }

    public boolean isLiked(String postId, String userId) {
        return jedis.sismember("post_likes:" + postId, userId);  // 중복 여부 확인
    }

    public int getRedisLikeCount(String postId) {
        String count = jedis.get("likes:" + postId);
        return count != null ? Integer.parseInt(count) : 0;
    }
}