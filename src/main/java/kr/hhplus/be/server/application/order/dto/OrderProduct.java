package kr.hhplus.be.server.application.order.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
    private Long productId;
    private int quantity;
    private BigDecimal price;
}
