package kr.hhplus.be.server.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderEntity;
import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="payment", indexes = {@Index(name = "idx_order_id", columnList = "order_id")})
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    OrderEntity order;

    @Column(nullable = false)
    LocalDate paymentDate;

    @Column(nullable = false)
    BigDecimal payAmount;

    @Column(nullable = false)
    PaymentStatus paymentStatus;

    String paymentFailureReason;

    @Column(nullable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

}
