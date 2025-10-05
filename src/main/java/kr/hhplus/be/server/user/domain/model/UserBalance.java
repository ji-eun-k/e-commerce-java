package kr.hhplus.be.server.user.domain.model;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.UserException;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class UserBalance {
    private Long id;
    private String name;
    private BigDecimal balance;

    // 잔액 충전
    public UserBalance chargeBalance(BigDecimal amount) {
        validateChargeAmount(amount);
        BigDecimal afterChargeBalance = balance.add(amount);
        validateAfterChargeAmount(afterChargeBalance);
        return new UserBalance(id, name, afterChargeBalance);
    }

    public UserBalance useBalance(BigDecimal amount) {
        validateUseAmount(amount);
        BigDecimal afterUseBalance = balance.subtract(amount);
        validateAfterUseAmount(afterUseBalance);
        return new UserBalance(id, name, afterUseBalance);
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

    // 사용 금액 검증
    private void validateUseAmount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new UserException(ErrorCode.INVALID_USE_AMOUNT);
        }
    }

    // 충전 후 금액 검증 (최대 보유 금액)
    private void validateAfterChargeAmount(BigDecimal afterChargeBalance) {
        if(afterChargeBalance.compareTo(BigDecimal.valueOf(10000000)) > 0) {
            throw new UserException(ErrorCode.EXCEED_MAX_BALANCE);
        }

    }

    // 사용 후 금액 검증
    private void validateAfterUseAmount(BigDecimal afterUseBalance) {
        if(afterUseBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new UserException(ErrorCode.INSUFFICIENT_BALANCE);
        }
    }


}
