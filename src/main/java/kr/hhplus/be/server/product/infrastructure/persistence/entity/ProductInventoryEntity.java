package kr.hhplus.be.server.product.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "product_inventory")
public class ProductInventoryEntity {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private ProductEntity productEntity;

    @Column(nullable = false)
    int inventory;

    @Column(nullable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;
}
