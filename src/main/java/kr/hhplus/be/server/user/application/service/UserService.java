package kr.hhplus.be.server.user.application.service;

import com.google.common.util.concurrent.Striped;
import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.UserException;
import kr.hhplus.be.server.user.domain.enumtype.TransactionType;
import kr.hhplus.be.server.user.domain.model.BalanceTransaction;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.application.port.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserPort userPort;

    private final Striped<Lock> stripedLocks = Striped.lazyWeakLock(1024);

    // 잔액 충전
    @Transactional
    public UserBalance chargeUserBalance(UserBalanceRequest userBalanceRequest){
        return executeWithLock(userBalanceRequest,
                userBalance -> userBalance.chargeBalance(userBalanceRequest.getAmount()),
                TransactionType.CHARGE);
    }

    // 잔액 사용
    @Transactional
    public UserBalance useUserBalance(UserBalanceRequest userBalanceRequest) {
       return executeWithLock(userBalanceRequest,
               userBalance -> userBalance.useBalance(userBalanceRequest.getAmount()),
               TransactionType.USE);
    }

    // 잔액 조회
    public UserBalance getUserBalance(Long userId){
        return userPort.getUserBalance(userId);
    }

    // 동시성 제어 - Lock 추가 템플릿 메서드 패턴 적용
    public UserBalance executeWithLock(UserBalanceRequest userBalanceRequest, Function<UserBalance, UserBalance> operation, TransactionType type){
        Lock lock = stripedLocks.get(userBalanceRequest.getId());
        boolean acquired = false;

        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);

            if (!acquired) {
                throw new ConcurrentModificationException("다른 거래가 진행 중입니다. 잠시 후 다시 시도해주세요.");
            }

            UserBalance userBalance = getUserBalance(userBalanceRequest.getId());
            UserBalance updated = operation.apply(userBalance);

            userPort.save(updated, type, userBalanceRequest.getAmount());

            return updated;
        } catch (InterruptedException e) {
            log.error("오류 발생");
            throw new RuntimeException(e);
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }


}
