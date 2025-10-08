package kr.hhplus.be.server.product;

import kr.hhplus.be.server.ServerApplication;
import kr.hhplus.be.server.product.application.port.ProductPort;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.product.domain.model.ProductInventory;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductInventoryEntity;
import kr.hhplus.be.server.product.infrastructure.persistence.repository.ProductInventoryJpaRepository;
import kr.hhplus.be.server.product.infrastructure.persistence.repository.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ServerApplication.class)
@Testcontainers
public class ProductServiceConcurrencyTest {
    @Autowired
    private ProductPort productPort;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductInventoryJpaRepository productInventoryJpaRepository;

    @Autowired
    private ProductService productService;

    private Long productId;

    @BeforeEach
    public void setUp() {


    }

    @Test
    public void 상품_동시재고차감_테스트() throws InterruptedException {

        ProductInventoryEntity productInventoryEntity = new ProductInventoryEntity(1L, 90); // 재고 90개
        ProductInventoryEntity productInventory = productInventoryJpaRepository.save(productInventoryEntity);


        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failedCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    ProductInventory entity = productService.decreaseProductInventory(1L, 1);
                    System.out.println(entity.getInventory());
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

        assertThat(failedCount.get()).isEqualTo(10);
        assertThat(successCount.get()).isEqualTo(90);


    }

}
