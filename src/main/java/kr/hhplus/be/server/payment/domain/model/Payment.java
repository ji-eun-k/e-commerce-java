package kr.hhplus.be.server.payment.domain.model;

import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {
    private Long id;
    private Long orderId;
    private Long userId;
    private UUID idempotencyKey;
    private LocalDate paymentDate;
    private BigDecimal payAmount;
    private PaymentStatus paymentStatus;
    private String paymentFailureReason;
    private LocalDateTime updatedAt;

    public static Payment from(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .userId(paymentRequest.getUserId())
                .idempotencyKey(paymentRequest.getIdempotencyKey())
                .paymentDate(LocalDate.now())
                .payAmount(paymentRequest.getTotalPrice())
                .paymentStatus(PaymentStatus.COMPLETED)
                .updatedAt(LocalDateTime.now()).build();
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
