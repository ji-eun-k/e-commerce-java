package kr.hhplus.be.server.order.infrastructure.persistence.repository;

import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailJpaRepository extends JpaRepository<OrderDetailEntity, Long> {
}
