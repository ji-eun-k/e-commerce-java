package kr.hhplus.be.server.product.infrastructure.persistence.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.product.domain.enumtype.ProductCategory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Column(nullable = false)
    ProductCategory category;

    @Column(nullable = false)
    String productName;

    @Column(nullable = false)
    BigDecimal price;

    @Column(nullable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;
}
