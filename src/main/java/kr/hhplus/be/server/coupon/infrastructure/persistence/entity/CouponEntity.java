package kr.hhplus.be.server.coupon.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponType;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponValidPeriodUnit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private CouponType couponType;

    @Column(nullable = false)
    private int discountValue;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int orderMinimumPrice;

    @Column(nullable=false)
    @ColumnDefault("0")
    private int discountLimitValue;

    @Column(nullable = false)
    private CouponValidPeriodUnit validPeriodUnit;

    @Column(nullable = false)
    private int validPeriodValue;

    private Long issuableQuantity;

    private Long issuedQuantity;

    private LocalDateTime updatedAt;

    @Builder
    public CouponEntity(Long id, String name, CouponType couponType, int discountValue, int orderMinimumPrice, int discountLimitValue, CouponValidPeriodUnit validPeriodUnit, int validPeriodValue, Long issuableQuantity, Long issuedQuantity) {
        this.id = id;
        this.name = name;
        this.couponType = couponType;
        this.discountValue = discountValue;
        this.orderMinimumPrice = orderMinimumPrice;
        this.discountLimitValue = discountLimitValue;
        this.validPeriodUnit = validPeriodUnit;
        this.validPeriodValue = validPeriodValue;
        this.issuableQuantity = issuableQuantity;
        this.issuedQuantity = issuedQuantity;
        this.updatedAt = LocalDateTime.now();
    }


}
