package kr.hhplus.be.server.payment.application.service;

import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.domain.repository.PaymentRepository;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        // 포인트 사용
        UserBalanceRequest userBalanceRequest = UserBalanceRequest.builder().id(paymentRequest.getUserId()).amount(paymentRequest.getTotalPrice()).build();
        UserBalance userBalance = userService.useUserBalance(userBalanceRequest);

        // 결제 내역 저장
        Payment payment = paymentRepository.save(Payment.from(paymentRequest));

        // 추후 이벤트 리스너로 주문 상태변경 이벤트 발행 (PENDING > COMPLETED)

        return PaymentResponse.of(payment);
    }
}
