package kr.hhplus.be.server.common;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.PaymentException;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentOrchestrator {
    private final UserService userService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {

        try {
            if (paymentService.checkIdempotencyKey(paymentRequest.getUserId(), paymentRequest.getIdempotencyKey())) {
                throw new PaymentException(ErrorCode.DUPLICATE_PAYMENT);
            }

            // 포인트 사용
            UserBalanceRequest userBalanceRequest = UserBalanceRequest.builder().id(paymentRequest.getUserId()).amount(paymentRequest.getTotalPrice()).build();
            UserBalance userBalance = userService.useUserBalance(userBalanceRequest);

            // 결제 내역 저장
            PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);

            // 주문 완료 처리
            orderService.completeOrder(paymentRequest.getOrderId());

            return paymentResponse;
        } catch (Exception e){
            // TODO : 주문 PENDING 상태일 시 재고 원복 후 주문 실패 처리 (이벤트 리스너 발행) 추가
            throw e;
        }

    }
}
