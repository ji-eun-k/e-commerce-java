package kr.hhplus.be.server.user.infrastructure.persistence.repository;

import kr.hhplus.be.server.user.infrastructure.persistence.entity.BalanceTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Long> {
}
