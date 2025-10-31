package kr.hhplus.be.server.payment.interfaces;

import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/api/v1/payments")
    public PaymentResponse createPayment(PaymentRequest paymentRequest){
        return paymentService.createPayment(paymentRequest);
    }
}
