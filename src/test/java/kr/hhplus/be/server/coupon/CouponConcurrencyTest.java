package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.ServerApplication;
import kr.hhplus.be.server.coupon.application.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.application.dto.CouponIssueResponse;
import kr.hhplus.be.server.coupon.application.service.CouponService;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponType;
import kr.hhplus.be.server.coupon.domain.enumtype.CouponValidPeriodUnit;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.infrastructure.persistence.entity.CouponEntity;
import kr.hhplus.be.server.coupon.infrastructure.persistence.repository.CouponJpaRepository;
import kr.hhplus.be.server.coupon.infrastructure.persistence.repository.IssuedCouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ServerApplication.class)
@Testcontainers
public class CouponConcurrencyTest {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private IssuedCouponJpaRepository issuedCouponJpaRepository;

    private Long couponId;

    @BeforeEach
    public void setUp() {
        couponJpaRepository.deleteAll();
        issuedCouponJpaRepository.deleteAll();

        CouponEntity couponEntity = CouponEntity.builder()
                .name("테스트 쿠폰")
                .couponType(CouponType.FIXED)
                .discountValue(10000)
                .orderMinimumPrice(0)
                .discountLimitValue(0)
                .validPeriodUnit(CouponValidPeriodUnit.DAY)
                .validPeriodValue(30)
                .issuableQuantity(50L)
                .issuedQuantity(0L).build();
        CouponEntity savedCouponEntity = couponJpaRepository.save(couponEntity);
        couponId = savedCouponEntity.getId();
    }

    @Test
    public void 쿠폰_경쟁발급_테스트() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            int userId = i;
            executorService.execute(() -> {
                try {
                    CouponIssueResponse couponIssueResponse = couponService.issueCoupon(new CouponIssueRequest((long) userId, couponId));
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failedCount.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        assertThat(successCount.get()).isEqualTo(50);
        assertThat(failedCount.get()).isEqualTo(50);

        Optional<CouponEntity> couponEntity = couponJpaRepository.findById(couponId);
        assertThat(couponEntity.isPresent()).isTrue();
        assertThat(couponEntity.get().getIssuedQuantity()).isEqualTo(50);
    }
}
