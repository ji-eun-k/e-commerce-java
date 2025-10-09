package kr.hhplus.be.server.payment.infrastructure.persistence.adapter;

import kr.hhplus.be.server.payment.application.port.PaymentPort;
import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.infrastructure.persistence.entity.PaymentEntity;
import kr.hhplus.be.server.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentPort {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment from) {
        PaymentEntity paymentEntity = toPaymentEntity(from);
        PaymentEntity saved = paymentJpaRepository.save(paymentEntity);
        from.assignId(saved.getId());
        return from;
    }

    @Override
    public boolean checkIdempotencyKey(Long userId, UUID idempotencyKey) {
        return paymentJpaRepository.existsByUserIdAndIdempotencyKey(userId, idempotencyKey);
    }

    private PaymentEntity toPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .idempotencyKey(payment.getIdempotencyKey())
                .payAmount(payment.getPayAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentStatus(payment.getPaymentStatus())
                .build();
    }
}
