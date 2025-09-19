package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.model.Order;

public interface OrderRepository {
    // 내부에서 Order 테이블, OrderDetail 테이블 모두 저장하고 orderId 반환
    Long save(Order of);
}
