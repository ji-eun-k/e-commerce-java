package kr.hhplus.be.server.coupon.domain.model;

import kr.hhplus.be.server.coupon.domain.enumtype.CouponStatus;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponValidPeriodUnit;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class IssuedCoupon {
    private Long id;
    private Long couponId;
    private Long userId;
    private CouponStatus couponStatus;
    private LocalDate issuedDate;
    private LocalDate validStartDate;
    private LocalDate validEndDate;
    private LocalDate usedDate;

    public static IssuedCoupon createIssueCoupon(Coupon coupon, Long userId) {
        return IssuedCoupon.builder()
                .couponId(coupon.getId())
                .userId(userId)
                .couponStatus(CouponStatus.AVAILABLE)
                .issuedDate(LocalDate.now())
                .validStartDate(LocalDate.now())
                .validEndDate(calculateValidEndDate(coupon))
                .build();
    }

    private static LocalDate calculateValidEndDate(Coupon coupon) {
        // 유효 종료 기간 계산. 당일 포함 n일이므로 -1일
        if(CouponValidPeriodUnit.DAY.equals(coupon.getValidPeriodUnit())){
            return LocalDate.now().plusDays(coupon.getValidPeriodValue() - 1);
        } else if (CouponValidPeriodUnit.MONTH.equals(coupon.getValidPeriodUnit())){
            return LocalDate.now().plusMonths(coupon.getValidPeriodValue()).minusDays(1);
        } else {
            return LocalDate.now().plusYears(coupon.getValidPeriodValue()).minusDays(1);
        }
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
