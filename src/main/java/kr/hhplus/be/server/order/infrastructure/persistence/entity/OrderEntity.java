package kr.hhplus.be.server.order.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.domain.enumtype.OrderStatus;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders", indexes = {@Index(name = "idx_user_id", columnList = "user_id"), @Index(name="idx_user_id_order_date", columnList = "user_id, order_date")})
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    UserEntity users;

    @Column(nullable = false)
    LocalDate orderDate;

    @Column(nullable = false)
    BigDecimal totalPrice;

    @Column(nullable = false)
    BigDecimal finalPrice;

    @Column(nullable = false)
    OrderStatus orderStatus;

    LocalDateTime voidedAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;
}
