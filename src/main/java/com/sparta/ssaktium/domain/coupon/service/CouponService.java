package com.sparta.ssaktium.domain.coupon.service;

import com.sparta.ssaktium.domain.coupon.enums.CouponStatus;
import com.sparta.ssaktium.domain.coupon.repository.CouponRepository;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        couponCode = couponCode.replace("\"","");
        redisTemplate.opsForValue().set(COUPON_PREFIX + couponCode, CouponStatus.ISSUED.name());
        redisTemplate.opsForValue().set(USER_COUPON_PREFIX + userId, couponCode);

        log.info("쿠폰 발급: userId: {}, couponCode: {}", userId, couponCode);

        return "쿠폰이 발급되었습니다: " + couponCode;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> applyCoupon(long userId, String couponCode) {
        Map<String, Object> response = new HashMap<>();

        // 사용자가 보유한 쿠폰인지 확인
        String issuedCouponCode = (String) redisTemplate.opsForValue().get(USER_COUPON_PREFIX + userId);
        issuedCouponCode = issuedCouponCode.replace("\"","");
        if (issuedCouponCode == null || !issuedCouponCode.trim().equals(couponCode.trim())) {
            response.put("valid", false);
            response.put("message", "유효하지 않은 쿠폰입니다.");
            return response;
        }



        // 유효한 쿠폰인 경우
        response.put("valid", true);
        response.put("discountAmount", 2000); // 할인 금액 2000원 설정

        // 쿠폰 상태를 사용 완료로 변경
        redisTemplate.opsForValue().set(COUPON_PREFIX + couponCode, CouponStatus.USED.name());

        log.info("쿠폰 적용 완료: userId: {}, couponCode: {}", userId, couponCode);

        return response;
    }
}

