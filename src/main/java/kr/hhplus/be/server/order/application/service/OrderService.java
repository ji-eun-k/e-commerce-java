package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.application.port.OrderPort;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class OrderService {
    private OrderPort orderPort;
    private PaymentService paymentService;

    public OrderResponse createOrder(Order order) {
        return orderPort.save(order);
    }

    /* 주문 취소 - 화면에서 주문 취소 OR 결제 실패 시 호출 */
    @Transactional
    public void cancelOrder(Long orderId){

    }
}
