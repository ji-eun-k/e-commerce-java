package kr.hhplus.be.server.order.infrastructure.persistence.repository;

import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
