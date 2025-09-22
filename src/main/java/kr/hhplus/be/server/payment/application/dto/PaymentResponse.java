package kr.hhplus.be.server.payment.application.dto;

import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import kr.hhplus.be.server.payment.domain.model.Payment;
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
