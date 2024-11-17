package com.sparta.ssaktium.domain.coupon.service;

import com.sparta.ssaktium.domain.coupon.entity.Coupon;
import com.sparta.ssaktium.domain.coupon.enums.CouponStatus;
import com.sparta.ssaktium.domain.coupon.repository.CouponRepository;
import com.sparta.ssaktium.domain.products.entity.Product;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.redisson.api.RLock;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String COUPON_PREFIX = "coupon:code:"; // 쿠폰 코드 Redis 키 Prefix
    private static final String USER_COUPON_PREFIX = "user:coupon:"; // 사용자 쿠폰 Redis 키 Prefix

    // 쿠폰 N 개 생성 (Redis에 저장)
    @Transactional
    public String createCoupons() {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();

        List<String> coupons = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            // 16자리 랜덤 문자열 생성 (쿠폰 코드)
            String couponCode = RandomStringUtils.randomAlphanumeric(16);
            long currentTime = System.currentTimeMillis(); // 발급 시간을 점수로 사용
            redisTemplate.opsForZSet().add(COUPON_PREFIX + "unissued", couponCode, currentTime);
            coupons.add(couponCode);
        }

        // DB에 쿠폰 저장
        // couponRepository.saveAll(coupons);

        return "쿠폰이 성공적으로 생성되었습니다.";
    }

    @Transactional
    public String issueCoupon(long userId) {

        // 이미 발급된 쿠폰이 있는지 확인
        if (redisTemplate.hasKey(USER_COUPON_PREFIX + userId)) {
            log.info("이미 발급된 쿠폰이 있습니다. userId: {}", userId);
            return "이미 발급된 쿠폰이 있습니다.";
        }

        // Lua 스크립트를 사용하여 쿠폰 발급
        String luaScript = "local couponCode = redis.call('ZRANGE', KEYS[1], 0, 0)[1] " +
                "if couponCode then " +
                "  redis.call('ZREM', KEYS[1], couponCode) " +
                "  return couponCode " +
                "else " +
                "  return nil " +
                "end";

        // Lua 스크립트 실행
        String couponCode = (String) redisTemplate.execute(
                (RedisCallback<Object>) connection -> {
                    byte[] result = connection.eval(
                            luaScript.getBytes(StandardCharsets.UTF_8),
                            ReturnType.VALUE,
                            1,
                            (COUPON_PREFIX + "unissued").getBytes(StandardCharsets.UTF_8)
                    );
                    return result != null ? new String(result, StandardCharsets.UTF_8) : null;
                }
        );

        if (couponCode == null) {
            log.info("선착순 이벤트가 종료되었습니다. userId: {}", userId);
            return "선착순 이벤트가 종료되었습니다.";
        }

        // 쿠폰을 사용자에게 발급 완료 상태로 변경
        redisTemplate.opsForValue().set(COUPON_PREFIX + couponCode, CouponStatus.ISSUED.name());
        redisTemplate.opsForValue().set(USER_COUPON_PREFIX + userId, couponCode);

        log.info("쿠폰 발급: userId: {}, couponCode: {}", userId, couponCode);

        return "쿠폰이 발급되었습니다: " + couponCode;
    }

}

