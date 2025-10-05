package kr.hhplus.be.server.user.infrastructure.persistence.adapter;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.UserException;
import kr.hhplus.be.server.user.application.port.UserPort;
import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private UserJpaRepository userRepo;

    @Override
    public UserBalance getUserBalance(Long id) {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return UserBalance.create(userEntity.getId(), userEntity.getBalance());
    }

    @Override
    public int chargeUserBalance(UserBalance userBalance) {
        return 0;
    }

    @Override
    public void insertUserBalanceHistory(Long id, TransactionType type, BigDecimal amount) {

    }

    @Override
    public int useUserBalance(UserBalance afterUseUserBalance) {
        return 0;
    }
}
