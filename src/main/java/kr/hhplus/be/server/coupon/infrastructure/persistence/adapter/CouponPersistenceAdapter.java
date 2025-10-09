package kr.hhplus.be.server.coupon.infrastructure.persistence.adapter;

import kr.hhplus.be.server.config.exception.CouponException;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.coupon.application.port.CouponPort;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.IssuedCoupon;
import kr.hhplus.be.server.coupon.infrastructure.persistence.entity.CouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.persistence.entity.IssuedCouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.persistence.repository.CouponJpaRepository;
import kr.hhplus.be.server.coupon.infrastructure.persistence.repository.IssuedCouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponPersistenceAdapter implements CouponPort {

    private final CouponJpaRepository couponJpaRepository;
    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public Coupon getCouponForUpdate(Long couponId) {
        CouponEntity couponEntity = couponJpaRepository.findByIdWithLock(couponId).orElseThrow(
                () -> new CouponException(ErrorCode.COUPON_NOT_FOUND)
        );
        return toCouponDomain(couponEntity);
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity couponEntity = couponJpaRepository.save(toCouponEntity(coupon));
        return toCouponDomain(couponEntity);
    }

    @Override
    public boolean checkUserIssuedCoupon(Long userId, Long couponId) {
        return issuedCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public IssuedCoupon issueCoupon(IssuedCoupon issuedCoupon) {
        IssuedCouponEntity couponEntity = toIssuedCouponEntity(issuedCoupon);
        IssuedCouponEntity saved = issuedCouponJpaRepository.save(couponEntity);
        issuedCoupon.assignId(saved.getId());
        return issuedCoupon;
    }

    private Coupon toCouponDomain(CouponEntity couponEntity) {
        return Coupon.builder()
                .id(couponEntity.getId())
                .name(couponEntity.getName())
                .couponType(couponEntity.getCouponType())
                .discountValue(couponEntity.getDiscountValue())
                .orderMinimumPrice(couponEntity.getOrderMinimumPrice())
                .discountLimitValue(couponEntity.getDiscountLimitValue())
                .validPeriodUnit(couponEntity.getValidPeriodUnit())
                .issuableQuantity(couponEntity.getIssuableQuantity())
                .issuedQuantity(couponEntity.getIssuedQuantity())
                .build();
    }

    private CouponEntity toCouponEntity(Coupon coupon) {
        return CouponEntity.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .couponType(coupon.getCouponType())
                .discountValue(coupon.getDiscountValue())
                .orderMinimumPrice(coupon.getOrderMinimumPrice())
                .discountLimitValue(coupon.getDiscountLimitValue())
                .validPeriodValue(coupon.getValidPeriodValue())
                .validPeriodUnit(coupon.getValidPeriodUnit())
                .issuableQuantity(coupon.getIssuableQuantity())
                .issuedQuantity(coupon.getIssuedQuantity()).build();
    }

    private IssuedCouponEntity toIssuedCouponEntity(IssuedCoupon issuedCoupon) {
        return IssuedCouponEntity.builder()
                .couponId(issuedCoupon.getCouponId())
                .userId(issuedCoupon.getUserId())
                .couponStatus(issuedCoupon.getCouponStatus())
                .issuedDate(issuedCoupon.getIssuedDate())
                .validStartDate(issuedCoupon.getValidStartDate())
                .validEndDate(issuedCoupon.getValidEndDate())
                .usedDate(issuedCoupon.getUsedDate())
                .build();
    }
}
