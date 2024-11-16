package com.sparta.ssaktium.domain.coupon.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 N 개 생성
    @Secured("ROLE_ADMIN")
    @PostMapping("/v2/create-coupons")
    public ResponseEntity<CommonResponse<String>> createCoupons() {
        return ResponseEntity.ok(CommonResponse.success(couponService.createCoupons()));
    }

    // 쿠폰 발급
//    @GetMapping("/v2/coupons")
//    public ResponseEntity<CommonResponse<String>> getCoupons(@AuthenticationPrincipal AuthUser authUser) {
//        return ResponseEntity.ok(CommonResponse.success(couponService.issueCoupon(authUser.getUserId())));
//    }

    @GetMapping("/v2/coupons")
    public ResponseEntity<CommonResponse<String>> getCoupons(@RequestParam(value = "userId", defaultValue = "1") long userId) {
        // 임시로 50명의 사용자 ID를 테스트할 수 있도록 랜덤으로 할당
        long testUserId = (userId % 50) + 1; // 1~50 범위의 값으로 userId를 설정
        return ResponseEntity.ok(CommonResponse.success(couponService.issueCoupon(testUserId)));
    }
}
