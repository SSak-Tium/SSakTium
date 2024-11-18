package com.sparta.ssaktium.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CouponLockService {

    private final CouponService couponService;
    private final RedissonClient redissonClient;

    public String issueCoupon(long userId) {
        // 분산락을 위한 락 키 설정
        String lockKey = "coupon:issue";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도 시간 2초, 락 유지 시간 3초
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                return "쿠폰 발급이 지연되고 있습니다. 잠시 후 다시 시도해주세요.";
            }

            return couponService.issueCoupon(userId);

        } catch (Exception e) {

        } finally {
            // 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return lockKey;
    }
}
