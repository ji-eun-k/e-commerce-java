package kr.hhplus.be.server.order.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.domain.enumtype.OrderStatus;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders", indexes = {@Index(name = "idx_user_id", columnList = "user_id"), @Index(name="idx_user_id_order_date", columnList = "user_id, order_date")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id", nullable=false)
//    UserEntity users;
    // 도메인 분리 / 물리 DB 분리를 위해 FK 삭제, 식별자만 저장
    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    LocalDate orderDate;

    @Column(nullable = false)
    BigDecimal totalPrice;

    @Column(nullable = false)
    BigDecimal finalPrice;

    // 적용 쿠폰 아이디 (도메인 분리/ 물리 DB 분리를 위해 FK 삭제, 식별자만 저장)
    Long issuedCouponId;

    @Column(nullable = false)
    OrderStatus orderStatus;

    LocalDateTime voidedAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

    @Builder
    public OrderEntity(Long id, Long userId, LocalDate orderDate, BigDecimal totalPrice, BigDecimal finalPrice, OrderStatus orderStatus, Long issuedCouponId, LocalDateTime voidedAt) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.orderStatus = orderStatus;
        this.issuedCouponId = issuedCouponId;
        this.voidedAt = voidedAt;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
