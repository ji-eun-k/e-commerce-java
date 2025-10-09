package kr.hhplus.be.server;

import kr.hhplus.be.server.common.OrderOrchestrator;
import kr.hhplus.be.server.common.PaymentOrchestrator;
import kr.hhplus.be.server.config.exception.PaymentException;
import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.enumtype.OrderStatus;
import kr.hhplus.be.server.order.infrastructure.persistence.entity.OrderEntity;
import kr.hhplus.be.server.order.infrastructure.persistence.external.MessageQueueProducer;
import kr.hhplus.be.server.order.infrastructure.persistence.repository.OrderDetailJpaRepository;
import kr.hhplus.be.server.order.infrastructure.persistence.repository.OrderJpaRepository;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.payment.domain.enumtype.PaymentStatus;
import kr.hhplus.be.server.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.product.domain.enumtype.ProductCategory;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductEntity;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductInventoryEntity;
import kr.hhplus.be.server.product.infrastructure.persistence.repository.ProductInventoryJpaRepository;
import kr.hhplus.be.server.product.infrastructure.persistence.repository.ProductJpaRepository;
import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.BalanceTransactionJpaRepository;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = ServerApplication.class)
@Testcontainers
public class OrderPaymentIntegrationTest {

    @Autowired
    private OrderOrchestrator orderOrchestrator;

    @Autowired
    private PaymentOrchestrator paymentOrchestrator;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @MockitoBean
    private MessageQueueProducer messageQueueProducer;


    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private BalanceTransactionJpaRepository balanceTransactionJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductInventoryJpaRepository productInventoryJpaRepository;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private OrderDetailJpaRepository orderDetailJpaRepository;

    private Long id;
    private BigDecimal amount;
    private Long productId1;
    private Long productId2;
    private OrderRequest orderRequest;


    @BeforeEach
    void setUp() {
        paymentJpaRepository.deleteAll();
        orderDetailJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
        productInventoryJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
        balanceTransactionJpaRepository.deleteAll();
        userJpaRepository.deleteAll();

        // 유저 정보 저장
        UserEntity userEntity = new UserEntity(null, "항해", BigDecimal.ZERO);
        userEntity = userJpaRepository.save(userEntity);
        id = userEntity.getId();
        amount = BigDecimal.valueOf(500000);

        // 상품 정보 저장
        ProductEntity productEntity1 = new ProductEntity(ProductCategory.FASHION, "바지", BigDecimal.valueOf(64000));
        ProductEntity savedEntity1= productJpaRepository.save(productEntity1);
        ProductEntity productEntity2 = new ProductEntity(ProductCategory.ELECTRONICS, "이북리더기", BigDecimal.valueOf(420000));
        ProductEntity savedEntity2= productJpaRepository.save(productEntity2);

        ProductInventoryEntity productInventoryEntity1 = new ProductInventoryEntity(savedEntity1.getId(), 5); // 재고 5개
        ProductInventoryEntity savedInventory = productInventoryJpaRepository.save(productInventoryEntity1);
        ProductInventoryEntity productInventoryEntity2 = new ProductInventoryEntity(productEntity2.getId(), 5);
        ProductInventoryEntity savedInventory2 = productInventoryJpaRepository.save(productInventoryEntity2);

        productId1 = savedEntity1.getId();
        productId2 = savedEntity2.getId();

        // 테스트용 주문 정보
        OrderItem orderItem1 = OrderItem.builder().productId(savedInventory.getProductId()).quantity(1).build();
        OrderItem orderItem2 = OrderItem.builder().productId(savedInventory2.getProductId()).quantity(1).build();

        orderRequest = OrderRequest.builder().userId(id).orderItems(List.of(orderItem1, orderItem2)).build();

    }

    @Test
    public void 잔액충전_주문생성_결제_잔액검증테스트(){
        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();
        UserBalance userBalance = userService.chargeUserBalance(chargeBalanceRequest);

        assertThat(userBalance.getBalance().compareTo(amount)).isEqualTo(0);

        OrderResponse orderResponse = orderOrchestrator.processOrder(orderRequest);

        // 상품 금액 검증
        assertThat(orderResponse.getTotalPrice().compareTo(BigDecimal.valueOf(484000))).isEqualTo(0);
        // 재고 검증
        Optional<ProductInventoryEntity> productInventoryEntity = productInventoryJpaRepository.findById(productId1);
        assertThat(productInventoryEntity.isPresent()).isTrue();
        assertThat(productInventoryEntity.get().getInventory()).isEqualTo(4);
        Optional<ProductInventoryEntity> productInventoryEntity2 = productInventoryJpaRepository.findById(productId2);
        assertThat(productInventoryEntity2.isPresent()).isTrue();
        assertThat(productInventoryEntity2.get().getInventory()).isEqualTo(4);

        UUID idempotencyKey = UUID.randomUUID();
        PaymentRequest paymentRequest = new PaymentRequest(orderResponse.getOrderId(), id, orderResponse.getTotalPrice(), idempotencyKey);

        PaymentResponse paymentResponse = paymentOrchestrator.processPayment(paymentRequest);

        // 주문 완료 검증
        assertThat(paymentResponse.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        Optional<OrderEntity> orderEntity = orderJpaRepository.findById(orderResponse.getOrderId());
        assertThat(orderEntity.isPresent()).isTrue();
        assertThat(orderEntity.get().getId()).isEqualTo(orderResponse.getOrderId());
        assertThat(orderEntity.get().getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);

        // 잔액 검증
        Optional<UserEntity> user = userJpaRepository.findById(id);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getBalance().compareTo(BigDecimal.valueOf(16000))).isEqualTo(0);

        // 외부 전송 검증
        verify(messageQueueProducer).sendMessage(orderResponse.getOrderId());

    }

    @Test
    public void 잔액충전_주문생성_결제_멱등성테스트(){
        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();
        UserBalance userBalance = userService.chargeUserBalance(chargeBalanceRequest);

        OrderResponse orderResponse = orderOrchestrator.processOrder(orderRequest);

        UUID idempotencyKey = UUID.randomUUID();
        PaymentRequest paymentRequest = new PaymentRequest(orderResponse.getOrderId(), id, orderResponse.getTotalPrice(), idempotencyKey);

        // 첫 결제 성공
        PaymentResponse paymentResponse = paymentOrchestrator.processPayment(paymentRequest);

        // 두 번째 결제 실패
        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> paymentOrchestrator.processPayment(paymentRequest)
        );

        assertThat(exception.getCode()).isEqualTo("DUPLICATE_PAYMENT");
        assertThat(exception.getMessage()).contains("이미 결제");

        // 외부 전송 검증
        verify(messageQueueProducer).sendMessage(orderResponse.getOrderId());

    }




}
