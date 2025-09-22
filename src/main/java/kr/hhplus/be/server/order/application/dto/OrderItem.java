package kr.hhplus.be.server.order.application.dto;

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
