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
    public ResponseEntity<CommonResponse<String>> getCoupons(@RequestParam long userId) {
        return ResponseEntity.ok(CommonResponse.success(couponService.issueCoupon(userId)));
    }
}
