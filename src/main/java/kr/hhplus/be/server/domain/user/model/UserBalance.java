package kr.hhplus.be.server.domain.user.model;

import kr.hhplus.be.server.domain.exception.ErrorCode;
import kr.hhplus.be.server.domain.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class UserBalance {
    private Long id;
    private BigDecimal balance;

    // 잔액 충전
    public UserBalance chargeBalance(BigDecimal amount) {
        validateChargeAmount(amount);
        BigDecimal afterChargeBalance = balance.add(amount);
        validateAfterChargeAmount(afterChargeBalance);
        return new UserBalance(id, afterChargeBalance);
    }

    // 충전 금액 검증
    private void validateChargeAmount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.valueOf(1000)) < 0) {
            throw new UserException(ErrorCode.MIN_CHARGE_AMOUNT);
        }

        if(amount.compareTo(BigDecimal.valueOf(5000000)) > 0){
            throw new UserException(ErrorCode.MAX_CHARGE_AMOUNT);
        }
    }

    // 충전 후 금액 검증 (최대 보유 금액)
    private void validateAfterChargeAmount(BigDecimal afterChargeBalance) {
        if(afterChargeBalance.compareTo(BigDecimal.valueOf(10000000)) > 0) {
            throw new UserException(ErrorCode.EXCEED_MAX_BALANCE);
        }

    }
}
