package kr.hhplus.be.server.order.application.dto;

import kr.hhplus.be.server.order.domain.model.Order;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalPrice;

    public static OrderResponse from(Long orderId, Order order) {
        return OrderResponse.builder().orderId(orderId).totalPrice(order.getTotalPrice()).build();
    }
}
