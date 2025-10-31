package kr.hhplus.be.server.order.application.dto;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.OrderException;
import kr.hhplus.be.server.config.exception.UserException;
import lombok.*;
import org.springframework.util.CollectionUtils;

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
    private List<OrderItem> orderItems;

    public void validation(){

        if(userId == null){
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }

        if(CollectionUtils.isEmpty(orderItems)) {
            throw new OrderException(ErrorCode.ORDER_PRODUCT_MISSING);
        }
    }
}
