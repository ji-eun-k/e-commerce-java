package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.product.application.dto.ProductOrderDetail;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.order.domain.enumtype.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private BigDecimal finalPrice;
    private Long issuedCouponId;
    private OrderStatus orderStatus;
    private LocalDateTime voidedAt;
    private LocalDateTime updatedAt;
    private List<ProductOrderDetail> productOrderDetails;


    public static Order of(OrderRequest orderRequest, ProductOrderResult productOrderResult){
        return Order.builder().userId(orderRequest.getUserId())
                .orderDate(LocalDate.now())
                .totalPrice(productOrderResult.getTotalPrice())
                .finalPrice(productOrderResult.getTotalPrice())
                .issuedCouponId(orderRequest.getIssuedCouponId())
                .productOrderDetails(productOrderResult.getProductOrderDetails())
                .orderStatus(OrderStatus.PENDING)
                .updatedAt(LocalDateTime.now()).build();
    }
}
