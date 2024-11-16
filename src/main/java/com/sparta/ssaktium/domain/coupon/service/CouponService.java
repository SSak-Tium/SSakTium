package com.sparta.ssaktium.domain.coupon.service;

import com.sparta.ssaktium.domain.coupon.entity.Coupon;
import com.sparta.ssaktium.domain.coupon.enums.CouponStatus;
import com.sparta.ssaktium.domain.coupon.repository.CouponRepository;
import com.sparta.ssaktium.domain.users.entity.User;
import com.sparta.ssaktium.domain.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserService userService;
    private final RedissonClient redissonClient;

    // 쿠폰 N 개 생성
    @Transactional
    public String createCoupons() {
        List<Coupon> coupons = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            // 16자리 랜덤 문자열 생성 ( 코드 )
            String couponCode = RandomStringUtils.randomAlphanumeric(16);

            // 객체 생성
            Coupon coupon = Coupon.createCoupon(couponCode);
            coupons.add(coupon);
        }

        // DB 저장
        couponRepository.saveAll(coupons);

        return "쿠폰이 성공적으로 생성되었습니다.";
    }

    @Transactional
    public String issueCoupon(long userId) {
        // 분산락을 위한 락 키 설정
        String lockKey = "coupon:issue";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도 시간 5초, 락 유지 시간 10초
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("쿠폰 발급 실패: 다른 작업이 진행 중입니다. userId: {}", userId);
                return "쿠폰 발급이 지연되고 있습니다. 잠시 후 다시 시도해주세요.";
            }

            User user = userService.findUser(userId);

            // 이미 발급된 쿠폰이 있는지 확인
            if (couponRepository.existsByUserAndCouponStatus(user, CouponStatus.ISSUED)) {
                log.info("이미 발급된 쿠폰이 있습니다. userId: {}", userId);
                return "이미 발급된 쿠폰이 있습니다.";
            }

            // 임의로 미발급 상태의 쿠폰 하나를 조회
            Coupon coupon = couponRepository.findTopByCouponStatus(CouponStatus.UNISSUED);

            // 모든 쿠폰이 소진된 경우
            if (coupon == null) {
                log.info("선착순 이벤트가 종료되었습니다. userId: {}", userId);
                return "선착순 이벤트가 종료되었습니다.";
            }

            // 쿠폰을 사용자에게 발급 완료 상태로 변경
            coupon.issueToUser(user);
            couponRepository.save(coupon);
            long countUnissuedCoupons = getUnissuedCouponCount();
            log.info("쿠폰 발급: userId: {}, couponCode: {}, 남은 쿠폰 수: {}", userId, coupon.getCode(), countUnissuedCoupons);

            return "쿠폰이 발급되었습니다: " + coupon.getCode();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("쿠폰 발급 중 인터럽트가 발생했습니다.", e);
            return "쿠폰 발급 중 오류가 발생했습니다. 다시 시도해 주세요.";
        } finally {
            // 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }




    // 남은 미발급 쿠폰 개수 조회 메서드
    public long getUnissuedCouponCount() {
        return couponRepository.countByCouponStatus(CouponStatus.UNISSUED);
    }
}
