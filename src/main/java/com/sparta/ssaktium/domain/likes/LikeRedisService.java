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

    public void incrementLike(String boardId, String userId) {
        jedis.incr("likes:" + boardId);  // 좋아요 수 증가
        jedis.sadd("board_likes:" + boardId, userId);  // 사용자 좋아요 기록 추가
    }

    public void decrementLike(String boardId, String userId){
        jedis.decr("likes:" + boardId); // 좋아요 수 감소
        jedis.srem("board_likes:" + boardId, userId);
    }

    public boolean isLiked(String boardId, String userId) {
        return jedis.sismember("board_likes:" + boardId, userId);  // 중복 여부 확인
    }

    // 좋아요 수 조회
    public int getRedisLikeCount(String boardId) {
        String count = jedis.get("likes:" + boardId);
        return count != null ? Integer.parseInt(count) : 0;
    }
}