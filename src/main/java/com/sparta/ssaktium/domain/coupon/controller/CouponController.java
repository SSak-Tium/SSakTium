package com.sparta.ssaktium.domain.coupon.controller;

import com.sparta.ssaktium.config.CommonResponse;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.coupon.dto.CouponCodeDto;
import com.sparta.ssaktium.domain.coupon.service.CouponLockService;
import com.sparta.ssaktium.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponLockService couponLockService;

    @GetMapping("/ssaktium/event")
    public String event() {
        return "event";
    }

    // 쿠폰 N 개 생성
    @Secured("ROLE_ADMIN")
    @PostMapping("/v2/create-coupons")
    public ResponseEntity<CommonResponse<String>> createCoupons() {
        return ResponseEntity.ok(CommonResponse.success(couponService.createCoupons()));
    }

    // 쿠폰 발급
    @GetMapping("/v2/coupons")
    @ResponseBody
    public Map<String, String> getCoupons(@AuthenticationPrincipal AuthUser authUser) {
        String message = couponService.issueCoupon(authUser.getUserId());

        Map<String, String> response = new HashMap<>();
        response.put("success", message.contains("발급되었습니다") ? "true" : "false");
        response.put("message", message);

        return response;
    }

//    @GetMapping("/v2/coupons")
//    public ResponseEntity<CommonResponse<String>> getCoupons(@RequestParam long userId) {
//        return ResponseEntity.ok(CommonResponse.success(couponLockService.issueCoupon(userId)));
//    }

    @PostMapping("/v2/coupons/apply")
    public ResponseEntity<Map<String, Object>> applyCoupon(@AuthenticationPrincipal AuthUser authUser, @RequestBody CouponCodeDto couponCodeDto) {

        Map<String, Object> response = couponService.applyCoupon(authUser.getUserId(), couponCodeDto.getCouponCode());

        return ResponseEntity.ok(response);
    }
}
