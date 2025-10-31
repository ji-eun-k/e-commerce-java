package kr.hhplus.be.server.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "balance_transaction", indexes = {@Index(name = "idx_user_id", columnList = "user_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceTransactionEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY) Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @Column(nullable = false)
    TransactionType transactionType;

    @Column(nullable = false)
    BigDecimal amount;

    @Column(nullable = false)
    BigDecimal balance;

    @CreatedDate
    LocalDateTime transactionAt;

    public BalanceTransactionEntity(UserEntity user, TransactionType transactionType, BigDecimal amount, BigDecimal balance) {
        this.user = user;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balance = balance;
    }

}
