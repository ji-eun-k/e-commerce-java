package kr.hhplus.be.server.user.domain.model;

import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BalanceTransaction {
    private Long userId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balance;
}
