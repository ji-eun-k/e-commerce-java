package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.application.port.OrderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderPort orderPort;

    // 주문 저장 (결제 대기 상태)
    public OrderResponse createOrder(Order order) {
        return orderPort.save(order);
    }

    // 주문 완료
    public void completeOrder(Long orderId) {
        orderPort.completeOrder(orderId);
    }

    /* 주문 취소 - 화면에서 주문 취소 OR 결제 실패 시 호출 */
    @Transactional
    public void cancelOrder(Long orderId){

    }
}
