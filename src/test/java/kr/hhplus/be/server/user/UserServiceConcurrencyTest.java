package kr.hhplus.be.server.user;

import kr.hhplus.be.server.ServerApplication;
import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.port.UserPort;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ServerApplication.class)
@Testcontainers
public class UserServiceConcurrencyTest {

    @Autowired
    private UserPort userPort;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private UserService userService;

    private Long id;
    private String name;
    private BigDecimal balance;
    private BigDecimal amount;


    @BeforeEach
    public void setUp() {
        name = "name";
        balance = BigDecimal.valueOf(2000000);
        amount = BigDecimal.valueOf(100000);
        userService = new UserService(userPort);

        // 테스트 전 미리 데이터 저장
        UserEntity userEntity = new UserEntity(null, name, balance);
        userEntity = userJpaRepository.save(userEntity);

        id = userEntity.getId();

    }

    @Test
    public void 잔액_충전_통합테스트() throws InterruptedException {
        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    UserBalance userBalance = userService.chargeUserBalance(chargeBalanceRequest);
                    System.out.println(userBalance.getBalance());
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failedCount.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        assertThat(successCount.get()).isEqualTo(80);
        assertThat(failedCount.get()).isEqualTo(20);
    }

    @Test
    public void 잔액_사용_통합테스트() throws InterruptedException {
        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    UserBalance userBalance = userService.useUserBalance(chargeBalanceRequest);
                    System.out.println(userBalance.getBalance());
                    successCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failedCount.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        assertThat(successCount.get()).isEqualTo(20);
        assertThat(failedCount.get()).isEqualTo(30);
    }
}
