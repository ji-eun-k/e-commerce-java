package kr.hhplus.be.server.order.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "order_detail", indexes = {@Index(name="idx_order_id", columnList = "order_id")})
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity product;

    @Column(nullable = false)
    int quantity;

    @Column(nullable = false)
    BigDecimal price;

    @Column(nullable = false)
    BigDecimal totalPrice;

}
