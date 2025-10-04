package kr.hhplus.be.server.payment.application.port;

import kr.hhplus.be.server.payment.domain.model.Payment;

public interface PaymentRepository {
    Payment save(Payment from);
}
