package kr.hhplus.be.server.application.product.dto;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOrderResult {
    private BigDecimal totalPrice;
    private List<ProductOrderDetail> productOrderDetails;

    public static ProductOrderResult of(BigDecimal totalPrice, List<ProductOrderDetail> productOrderDetails) {
        return new ProductOrderResult(totalPrice, productOrderDetails);
    }
}
