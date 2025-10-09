package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.config.exception.CouponException;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.coupon.application.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.application.dto.CouponIssueResponse;
import kr.hhplus.be.server.coupon.application.port.CouponPort;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.IssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponPort couponPort;

    @Transactional
    public CouponIssueResponse issueCoupon(CouponIssueRequest couponRequest){
        // 이미 회원이 발급 받은 쿠폰인지 확인
        if(couponPort.checkUserIssuedCoupon(couponRequest.getUserId(), couponRequest.getCouponId())){
            throw new CouponException(ErrorCode.EXCEED_MAX_COUPON); // 1개만 다운로드 받을 수 있음
        }
        // 업데이트 전 쿠폰 조회
        Coupon coupon = couponPort.getCouponForUpdate(couponRequest.getCouponId());
        coupon.issue();
        // 쿠폰 장수 업데이트
        Coupon saved = couponPort.save(coupon);
        // 회원 쿠폰 발급
        IssuedCoupon issuedCoupon = IssuedCoupon.createIssueCoupon(coupon, couponRequest.getUserId());
        issuedCoupon = couponPort.issueCoupon(issuedCoupon);
        return new CouponIssueResponse(issuedCoupon.getCouponId());
    }
}
