package kr.hhplus.be.server.coupon.infrastructure.persistence.repository;

import kr.hhplus.be.server.coupon.infrastructure.persistence.entity.IssuedCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCouponEntity, Long> {
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
