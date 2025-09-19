package kr.hhplus.be.server.application.order.dto;

import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequest {
    private Long userId;
    private Long issuedCouponId;
    private Long totalPrice;
    private List<OrderProduct> orderProducts;
}
