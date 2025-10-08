package kr.hhplus.be.server.order.application.port;

import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.domain.model.Order;

import java.util.List;

public interface OrderPort {
    // 내부에서 Order 테이블, OrderDetail 테이블 모두 저장하고 orderId 반환
    OrderResponse save(Order order);
}
