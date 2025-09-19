package kr.hhplus.be.server.application.order.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    private Long productId;
    private int quantity;
}
