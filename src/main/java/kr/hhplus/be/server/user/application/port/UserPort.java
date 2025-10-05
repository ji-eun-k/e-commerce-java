package kr.hhplus.be.server.user.application.port;

import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import kr.hhplus.be.server.user.domain.model.BalanceTransaction;
import kr.hhplus.be.server.user.domain.model.UserBalance;

import java.math.BigDecimal;

public interface UserPort {

    // 잔액 조회
    UserBalance getUserBalance(Long id);

    // 현재 잔액 저장
    UserBalance save(UserBalance userBalance, TransactionType transactionType, BigDecimal amount);
}
