package kr.hhplus.be.server.payment.application.dto;

import kr.hhplus.be.server.order.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;
    private UUID idempotencyKey;

    public static PaymentRequest of(Order order) {
        return PaymentRequest.builder().userId(order.getUserId()).orderId(order.getId()).totalPrice(order.getTotalPrice()).build();
    }
}
