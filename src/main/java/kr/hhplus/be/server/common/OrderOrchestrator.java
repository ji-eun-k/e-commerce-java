package kr.hhplus.be.server.common;

import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OrderOrchestrator {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CouponService couponService;

    @Transactional
    public OrderResponse processOrder(OrderRequest orderRequest) {
        orderRequest.validation();

        // 유저 검증을 위해 호출
        userService.getUserBalance(orderRequest.getUserId());

        // totalPrice 계산가져오기
        ProductOrderResult productOrderResult = productService.getProductOrderPrice(orderRequest.getOrderItems());
        BigDecimal finalPrice = productOrderResult.getTotalPrice();

        // 쿠폰 적용
        if (orderRequest.getIssuedCouponId() != null) {
            Coupon coupon = couponService.selectIssuedCoupon(orderRequest.getIssuedCouponId());
            finalPrice = coupon.calculateDiscountAmount(productOrderResult.getTotalPrice());
            couponService.updateCouponStatus(orderRequest.getIssuedCouponId());
        }

        // 저장
        Order order = Order.of(orderRequest, productOrderResult, finalPrice);
        // 재고 차감
        for (OrderItem orderItem : orderRequest.getOrderItems()) {
            productService.decreaseProductInventory(orderItem.getProductId(), orderItem.getQuantity());
        }

        return orderService.createOrder(order);

    }

}
