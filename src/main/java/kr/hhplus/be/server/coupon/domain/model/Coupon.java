package kr.hhplus.be.server.coupon.domain.model;


import kr.hhplus.be.server.config.exception.CouponException;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponType;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponValidPeriodUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Coupon {
    private Long id;

    private String name;

    private CouponType couponType;

    private int discountValue;

    private int orderMinimumPrice;

    private int discountLimitValue;

    private CouponValidPeriodUnit validPeriodUnit;

    private int validPeriodValue;

    private Long issuableQuantity;

    private Long issuedQuantity;

    public void issue(){
        // 다운로드 가능한 쿠폰 없음
        if (issuableQuantity != 0 && issuableQuantity.equals(issuedQuantity)){
            throw new CouponException(ErrorCode.EXCEED_COUPON_LIMIT);
        }

        this.issuedQuantity += 1;
    }
}
