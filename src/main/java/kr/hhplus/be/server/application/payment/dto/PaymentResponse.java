package kr.hhplus.be.server.application.payment.dto;

import kr.hhplus.be.server.domain.payment.enumtype.PaymentStatus;
import kr.hhplus.be.server.domain.payment.model.Payment;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PaymentResponse {
    private Long paymentId;
    private Long orderId;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;

    public static PaymentResponse of(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentDate(payment.getPaymentDate())
                .paymentStatus(payment.getPaymentStatus()).build();
    }
}
