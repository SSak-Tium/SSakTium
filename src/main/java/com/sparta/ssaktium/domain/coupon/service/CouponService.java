package com.sparta.ssaktium.domain.coupon.service;

import com.sparta.ssaktium.domain.coupon.entity.Coupon;
import com.sparta.ssaktium.domain.coupon.enums.CouponStatus;
import com.sparta.ssaktium.domain.coupon.repository.CouponRepository;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        List<Coupon> coupons = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            // 16자리 랜덤 문자열 생성 (쿠폰 코드)
            String couponCode = RandomStringUtils.randomAlphanumeric(16);
            long currentTime = System.currentTimeMillis(); // 발급 시간을 점수로 사용
            redisTemplate.opsForZSet().add(COUPON_PREFIX + "unissued", couponCode, currentTime);
        }

        // DB에 쿠폰 저장
        couponRepository.saveAll(coupons);

        return "쿠폰이 성공적으로 생성되었습니다.";
    }

    // 쿠폰 발급
// 쿠폰 발급 처리
    @Transactional
    public String issueCoupon(long userId) {

        // 이미 발급된 쿠폰이 있는지 확인
        if (redisTemplate.hasKey(USER_COUPON_PREFIX + userId)) {
            log.info("이미 발급된 쿠폰이 있습니다. userId: {}", userId);
            return "이미 발급된 쿠폰이 있습니다.";
        }

        // Redis Sorted Set에서 미발급 쿠폰 코드 조회
        String couponCode = getUnissuedCouponFromSortedSet();

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

    // 미발급 쿠폰 코드 하나를 Sorted Set에서 임의로 조회
    private String getUnissuedCouponFromSortedSet() {
        Set<Object> unissuedCoupons = redisTemplate.opsForZSet().range(COUPON_PREFIX + "unissued", 0, 0);
        if (unissuedCoupons.isEmpty()) {
            return null;
        }
        String couponCode = (String) unissuedCoupons.iterator().next();
        redisTemplate.opsForZSet().remove(COUPON_PREFIX + "unissued", couponCode); // 발급되었으면 제거
        return couponCode;
    }

}
