package kr.hhplus.be.server.order.infrastructure.persistence.adapter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.application.port.OrderPort;
import kr.hhplus.be.server.order.domain.enumtype.OrderStatus;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderDetailEntity;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderEntity;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.QOrderEntity;
import kr.hhplus.be.server.order.infrastructure.persistence.repository.OrderDetailJpaRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.repository.OrderJpaRepository;
import kr.hhplus.be.server.product.application.dto.ProductOrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDetailJpaRepository orderDetailJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public OrderResponse save(Order order) {
        // 주문 마스터 테이블 저장
        OrderEntity orderEntity = toOrderEntity(order);
        OrderEntity saved = orderJpaRepository.save(orderEntity);

        //주문 디테일 테이블 저장
        for(ProductOrderDetail orderDetail: order.getProductOrderDetails()) {
            OrderDetailEntity orderDetailEntity = toOrderDetailEntity(orderDetail, saved);
            orderDetailJpaRepository.save(orderDetailEntity);
        }

        return OrderResponse.from(saved.getId(), order);
    }

    @Override
    public void completeOrder(Long orderId) {
        QOrderEntity order = QOrderEntity.orderEntity;
        queryFactory.update(order)
                .set(order.orderStatus, OrderStatus.COMPLETED)
                .where(order.id.eq(orderId))
                .execute();
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

    private OrderDetailEntity toOrderDetailEntity(ProductOrderDetail orderItem, OrderEntity orderEntity){
        return OrderDetailEntity.builder()
                .order(orderEntity)
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .totalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .build();
    }
}
