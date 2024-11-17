package com.sparta.ssaktium.domain.coupon.entity;

import com.sparta.ssaktium.domain.coupon.enums.CouponStatus;
import com.sparta.ssaktium.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private CouponStatus couponStatus;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Coupon(String code, CouponStatus couponStatus) {
        this.code = code;
        this.couponStatus = couponStatus;
    }

    // 쿠폰 생성 메서드
    public static Coupon createCoupon(String code) {
        return new Coupon(code, CouponStatus.UNISSUED);
    }

    // 쿠폰 발급 완료 메서드
    public void issueToUser(User user) {
        this.couponStatus = CouponStatus.ISSUED;
        this.user = user;
    }
}
