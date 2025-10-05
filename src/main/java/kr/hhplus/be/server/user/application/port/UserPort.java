package kr.hhplus.be.server.user.application.port;

import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import kr.hhplus.be.server.user.domain.model.UserBalance;

import java.math.BigDecimal;

public interface UserPort {

    UserBalance getUserBalance(Long id);

    void insertUserBalanceHistory(Long id, TransactionType type, BigDecimal amount);

    int save(UserBalance userBalance);
}
