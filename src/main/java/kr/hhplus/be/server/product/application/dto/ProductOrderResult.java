package kr.hhplus.be.server.product.application.dto;

import lombok.*;

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
