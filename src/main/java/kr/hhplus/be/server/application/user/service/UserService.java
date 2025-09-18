package kr.hhplus.be.server.application.user.service;

import com.google.common.util.concurrent.Striped;
import kr.hhplus.be.server.application.user.dto.UserBalanceRequest;
import kr.hhplus.be.server.domain.exception.ErrorCode;
import kr.hhplus.be.server.domain.exception.UserException;
import kr.hhplus.be.server.domain.user.enumtype.TransactionType;
import kr.hhplus.be.server.domain.user.model.UserBalance;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final Striped<Lock> stripedLocks = Striped.lazyWeakLock(1024);

    @Transactional
    public UserBalance chargeUserBalance(UserBalanceRequest userBalanceRequest){
        Lock lock = stripedLocks.get(userBalanceRequest.getId());

        boolean acquired = false;

        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);

            if (!acquired) {
                throw new ConcurrentModificationException(
                        "다른 거래가 진행 중입니다. 잠시 후 다시 시도해주세요."
                );
            }

            UserBalance userBalance = userRepository.getUserBalance(userBalanceRequest.getId());
            if (ObjectUtils.isEmpty(userBalance)) {
                throw new UserException(ErrorCode.USER_NOT_FOUND);
            }

            UserBalance afterChargeUserBalance = userBalance.chargeBalance(userBalanceRequest.getAmount());
            int cnt = userRepository.chargeUserBalance(afterChargeUserBalance); // 잔액 충전

            if (cnt == 0) {
                throw new UserException(ErrorCode.CHARGE_BALANCE_FAILED);
            }
            userRepository.insertUserBalanceHistory(afterChargeUserBalance.getId(), TransactionType.CHARGE, userBalanceRequest.getAmount());

            return afterChargeUserBalance;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }

    }
}
