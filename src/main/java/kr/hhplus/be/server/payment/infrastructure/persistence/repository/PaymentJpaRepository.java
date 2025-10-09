package kr.hhplus.be.server.payment.infrastructure.persistence.repository;

import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import kr.hhplus.be.server.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    boolean existsByUserIdAndIdempotencyKey(Long userId, UUID idempotencyKey);
}
