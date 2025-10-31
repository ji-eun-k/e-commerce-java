package kr.hhplus.be.server.coupon.domain.model;


import kr.hhplus.be.server.config.exception.CouponException;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponType;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponValidPeriodUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public BigDecimal calculateDiscountAmount(BigDecimal amount){

        if(BigDecimal.valueOf(orderMinimumPrice).compareTo(amount) > 0){
            throw new CouponException(ErrorCode.MIN_PRICE_ERROR);
        }

        if (CouponType.PERCENTAGE == couponType){
            BigDecimal discountAmount = amount.multiply(BigDecimal.valueOf(discountValue)).divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR);

            if(discountAmount.compareTo(BigDecimal.ZERO) != 0 && discountAmount.compareTo(BigDecimal.valueOf(discountLimitValue)) > 0){
                discountAmount = BigDecimal.valueOf(discountLimitValue);
            }

            return amount.subtract(discountAmount);

        } else {

            return amount.subtract(BigDecimal.valueOf(discountValue));
        }
    }
}
