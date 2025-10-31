package kr.hhplus.be.server.coupon.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "issued_coupon", indexes = {@Index(name = "idx_user_id", columnList = "user_id"), @Index(name="idx_user_id_coupon_id", columnList = "user_id, coupon_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedCouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long couponId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private CouponStatus couponStatus;

    @Column(nullable = false)
    private LocalDate issuedDate;

    @Column(nullable = false)
    private LocalDate validStartDate;

    @Column(nullable = false)
    private LocalDate validEndDate;

    private LocalDate usedDate;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public IssuedCouponEntity(Long couponId, Long userId, CouponStatus couponStatus, LocalDate issuedDate, LocalDate validStartDate, LocalDate validEndDate, LocalDate usedDate) {
        this.couponId = couponId;
        this.userId = userId;
        this.couponStatus = couponStatus;
        this.issuedDate = issuedDate;
        this.validStartDate = validStartDate;
        this.validEndDate = validEndDate;
        this.usedDate = usedDate;
        this.updatedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public Long getCouponId() {
        return couponId;
    }
}
