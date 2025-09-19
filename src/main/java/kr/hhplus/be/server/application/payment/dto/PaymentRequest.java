package kr.hhplus.be.server.application.payment.dto;

import kr.hhplus.be.server.domain.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long orderId;
    private Long userId;
    private BigDecimal totalPrice;

    public static PaymentRequest of(Order order) {
        return PaymentRequest.builder().userId(order.getUserId()).orderId(order.getId()).totalPrice(order.getTotalPrice()).build();
    }
}
