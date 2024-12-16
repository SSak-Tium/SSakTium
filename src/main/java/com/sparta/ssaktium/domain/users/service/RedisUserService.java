package com.sparta.ssaktium.domain.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisUserService {

    private final RedisTemplate<String, String> redisTemplate;

    // Redis 에 Refresh Token 저장
    public void saveRefreshToken(String userId, String refreshToken, long ttlInMillis) {
        redisTemplate.opsForValue().set(userId, refreshToken, ttlInMillis, TimeUnit.MILLISECONDS);
    }

    // Refresh Token 조회
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    // Access Token 갱신
    public void updateAccessToken(String userId, String newAccessToken) {
        redisTemplate.opsForValue().set(userId, newAccessToken, 30, TimeUnit.MINUTES);
    }

    // Refresh Token 삭제
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }
}
