package kr.hhplus.be.server.payment.application.port;

import kr.hhplus.be.server.payment.domain.model.Payment;

import java.util.UUID;

public interface PaymentPort {
    Payment save(Payment from);

    // 멱등성 보장을 위해 중복으로 결제된 내역 있는지 확인
    boolean checkIdempotencyKey(Long userId, UUID idempotencyKey);
}
