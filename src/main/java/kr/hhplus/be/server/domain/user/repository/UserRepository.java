package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.domain.user.enumtype.TransactionType;
import kr.hhplus.be.server.domain.user.model.UserBalance;

import java.math.BigDecimal;

public interface UserRepository {

    UserBalance getUserBalance(Long id);

    int chargeUserBalance(UserBalance userBalance);

    void insertUserBalanceHistory(Long id, TransactionType type, BigDecimal amount);

}
