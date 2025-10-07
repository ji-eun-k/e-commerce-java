package kr.hhplus.be.server.order.infrastructure.persistence.adapter;

import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.application.port.OrderPort;
import kr.hhplus.be.server.order.domain.enumtype.OrderStatus;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderDetailEntity;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderEntity;
import kr.hhplus.be.server.order.infrastructure.persistence.repository.OrderDetailJpaRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDetailJpaRepository orderDetailJpaRepository;

    @Override
    public OrderResponse save(Order order) {
        OrderEntity orderEntity = toOrderEntity(order);
        OrderEntity saved = orderJpaRepository.save(orderEntity);
        return OrderResponse.from(saved.getId(), order);
    }

    private OrderEntity toOrderEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .finalPrice(order.getFinalPrice())
                .issuedCouponId(order.getIssuedCouponId())
                .voidedAt(order.getVoidedAt())
                .build();
    }
}
