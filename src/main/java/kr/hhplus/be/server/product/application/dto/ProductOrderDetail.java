package kr.hhplus.be.server.product.application.dto;

import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.ProductException;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOrderDetail {
    private Long productId;
    private int quantity;
    private BigDecimal price;

    public static ProductOrderDetail of(OrderItem orderItem, BigDecimal price) {

        if (price == null){
            throw new ProductException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        ProductOrderDetail productOrderDetail = new ProductOrderDetail();
        productOrderDetail.setProductId(orderItem.getProductId());
        productOrderDetail.setQuantity(orderItem.getQuantity());
        productOrderDetail.setPrice(price);
        return productOrderDetail;
    }
}
