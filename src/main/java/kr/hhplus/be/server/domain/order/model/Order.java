package kr.hhplus.be.server.domain.order.model;

import kr.hhplus.be.server.application.order.dto.OrderRequest;
import kr.hhplus.be.server.application.product.dto.ProductOrderResult;
import kr.hhplus.be.server.domain.order.enumtype.OrderStatus;
import lombok.*;
import org.checkerframework.checker.units.qual.N;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class Order {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalPrice;
    private Long issuedCouponId;
    private OrderStatus orderStatus;
    private LocalDateTime voidedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static Order of(OrderRequest orderRequest, ProductOrderResult productOrderResult){
        return Order.builder().userId(orderRequest.getUserId())
                .orderDate(LocalDate.now())
                .totalPrice(productOrderResult.getTotalPrice())
                .issuedCouponId(orderRequest.getIssuedCouponId())
                .orderStatus(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
    }
}
