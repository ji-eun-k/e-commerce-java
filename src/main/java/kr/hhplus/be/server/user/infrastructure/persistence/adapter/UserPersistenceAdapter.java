package kr.hhplus.be.server.user.infrastructure.persistence.adapter;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.UserException;
import kr.hhplus.be.server.user.application.port.UserPort;
import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.BalanceTransactionEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.BalanceTransactionJpaRepository;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final UserJpaRepository userRepository;
    private final BalanceTransactionJpaRepository balanceTransactionJpaRepository;

    // 잔액 조회
    @Override
    public UserBalance getUserBalance(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return toUserDomain(userEntity);
    }

    // 현재 잔액 저장
    @Override
    public UserBalance save(UserBalance userBalance, TransactionType transactionType, BigDecimal amount) {
        UserEntity userEntity = toUserEntity(userBalance);
        UserEntity saved = userRepository.save(userEntity);
        BalanceTransactionEntity balanceEntity = new BalanceTransactionEntity(saved, transactionType, amount, userBalance.getBalance());
        balanceTransactionJpaRepository.save(balanceEntity);
        return toUserDomain(saved);
    }

    // 도메인 변환
    private UserBalance toUserDomain(UserEntity userEntity) {
       return new UserBalance(userEntity.getId(), userEntity.getName(), userEntity.getBalance());
    }

    // 엔티티변환
    private UserEntity toUserEntity(UserBalance userBalance) {
        return new UserEntity(userBalance.getId(), userBalance.getName(), userBalance.getBalance());
    }
}
