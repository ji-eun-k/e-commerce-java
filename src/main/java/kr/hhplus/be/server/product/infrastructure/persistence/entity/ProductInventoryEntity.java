package kr.hhplus.be.server.product.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "product_inventory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInventoryEntity {
    @Id
    private Long productId; // 해당 entity에서 자주 변경이 일어나기 때문에 Product와의 명시적인 FK 관계 해제

    @Column(nullable = false)
    int inventory;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

    public ProductInventoryEntity(Long productId, int inventory) {
        this.productId = productId;
        this.inventory = inventory;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getProductId() {
        return productId;
    }

    public int getInventory() {
        return inventory;
    }
}
