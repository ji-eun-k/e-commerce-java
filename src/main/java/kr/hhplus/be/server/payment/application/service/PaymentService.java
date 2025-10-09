package kr.hhplus.be.server.payment.application.service;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.PaymentException;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.application.port.PaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentPort paymentPort;
    private final UserService userService;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        // 결제 내역 저장
        Payment payment = paymentPort.save(Payment.from(paymentRequest));

        return PaymentResponse.of(payment);
    }

    public Boolean checkIdempotencyKey(Long userId, UUID idempotencyKey){
        // 멱등성 체크
        return paymentPort.checkIdempotencyKey(userId, idempotencyKey);
    }
}
