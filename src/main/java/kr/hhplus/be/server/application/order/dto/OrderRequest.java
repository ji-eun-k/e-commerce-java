package kr.hhplus.be.server.application.order.dto;

import kr.hhplus.be.server.domain.exception.ErrorCode;
import kr.hhplus.be.server.domain.exception.OrderException;
import kr.hhplus.be.server.domain.exception.UserException;
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
