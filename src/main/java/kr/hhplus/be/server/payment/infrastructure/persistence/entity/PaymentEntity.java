package kr.hhplus.be.server.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderEntity;
import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="payment", indexes = {@Index(name = "idx_order_id", columnList = "order_id")},
uniqueConstraints = @UniqueConstraint(name="unique_payment", columnNames = {"user_id", "idempotency_key"}))
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    OrderEntity order;
    @Column(nullable = false)
    Long orderId;

    @Column(nullable = false)
    Long userId;

    @Column(nullable=false)
    UUID idempotencyKey;

    @Column(nullable = false)
    LocalDate paymentDate;

    @Column(nullable = false)
    BigDecimal payAmount;

    @Column(nullable = false)
    PaymentStatus paymentStatus;

    String paymentFailureReason;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

    @Builder
    public PaymentEntity(Long orderId, Long userId, UUID idempotencyKey, LocalDate paymentDate, BigDecimal payAmount, PaymentStatus paymentStatus, String paymentFailureReason) {
        this.orderId = orderId;
        this.userId = userId;
        this.idempotencyKey = idempotencyKey;
        this.paymentDate = paymentDate;
        this.payAmount = payAmount;
        this.paymentStatus = paymentStatus;
        this.paymentFailureReason = paymentFailureReason;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }

}
