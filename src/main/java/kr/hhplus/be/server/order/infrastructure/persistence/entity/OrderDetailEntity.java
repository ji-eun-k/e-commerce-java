package kr.hhplus.be.server.order.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail", indexes = {@Index(name="idx_order_id", columnList = "order_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    OrderEntity order;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", nullable = false)
//    ProductEntity product; FK 관계 해제 (물리 DB 분리 가능, 의존성 제거)
    @Column(nullable = false)
    Long productId;

    @Column(nullable = false)
    int quantity;

    @Column(nullable = false)
    BigDecimal price;

    @Column(nullable = false)
    BigDecimal totalPrice;

    @Builder
    public OrderDetailEntity(OrderEntity order, Long productId, int quantity, BigDecimal price, BigDecimal totalPrice) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

}
