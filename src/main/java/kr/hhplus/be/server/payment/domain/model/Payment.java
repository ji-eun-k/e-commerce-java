package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {
    private Long id;
    private Long orderId;
    private LocalDate paymentDate;
    private BigDecimal payAmount;
    private PaymentStatus paymentStatus;
    private String paymentFailureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Payment from(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .paymentDate(LocalDate.now())
                .payAmount(paymentRequest.getTotalPrice())
                .paymentStatus(PaymentStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
    }

    void assignId(Long id) {
        this.id = id;
    }
}
