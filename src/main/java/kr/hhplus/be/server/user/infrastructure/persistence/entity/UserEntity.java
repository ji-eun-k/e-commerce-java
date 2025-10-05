package kr.hhplus.be.server.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="users")
@NoArgsConstructor
public class UserEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY) Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    BigDecimal balance;

    @Column(nullable = false)
    @CreatedDate LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate LocalDateTime updatedAt;

    public UserEntity(Long id, String name, BigDecimal balance){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }
}

