package kr.hhplus.be.server.coupon.application.port;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.IssuedCoupon;

public interface CouponPort {
    Coupon getCouponForUpdate(Long couponId);

    Coupon save(Coupon coupon);

    boolean checkUserIssuedCoupon(Long userId, Long couponId);

    IssuedCoupon issueCoupon(IssuedCoupon issuedCoupon);
}
