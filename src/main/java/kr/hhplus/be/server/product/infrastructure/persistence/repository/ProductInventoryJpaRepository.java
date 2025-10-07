package kr.hhplus.be.server.product.infrastructure.persistence.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInventoryJpaRepository extends JpaRepository<ProductInventoryEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적락으로 재고 정합성 확보
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    Optional<ProductInventoryEntity> findByProductId(Long productId);
}
