package kr.hhplus.be.server.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="users")
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

}

