package kr.hhplus.be.server.payment;

import kr.hhplus.be.server.common.PaymentOrchestrator;
import kr.hhplus.be.server.config.exception.PaymentException;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.infrastructure.persistence.external.MessageQueueProducer;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.application.port.PaymentPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentOrchestrator paymentOrchestrator;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentPort paymentPort;

    @Mock
    private MessageQueueProducer messageQueueProducer;

    private Long userId;
    private Long orderId;
    private BigDecimal amount;
    private UUID idempotencyKey;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        orderId = 1L;
        amount = BigDecimal.valueOf(70000);
        idempotencyKey = UUID.randomUUID();
    }

    @Test
    public void 결제_테스트(){
        PaymentRequest paymentRequest = new PaymentRequest(userId, orderId, amount, idempotencyKey);
        Payment payment = Payment.from(paymentRequest);
        Payment paymentSetId = Payment.from(paymentRequest);
        paymentSetId.setId(2L);

        PaymentResponse paymentResponse = PaymentResponse.of(paymentSetId);

        when(paymentService.createPayment(paymentRequest)).thenReturn(paymentResponse);
        when(paymentService.checkIdempotencyKey(userId, idempotencyKey)).thenReturn(false);

        PaymentResponse paymentResult = paymentOrchestrator.processPayment(paymentRequest);

        assertThat(paymentResult.getOrderId()).isEqualTo(orderId);
        assertThat(paymentResult.getPaymentId()).isEqualTo(2L);

    }

    @Test
    public void 결제_테스트_실패(){
        PaymentRequest paymentRequest = new PaymentRequest(userId, orderId, amount, idempotencyKey);
        Payment payment = Payment.from(paymentRequest);
        Payment paymentSetId = Payment.from(paymentRequest);
        paymentSetId.setId(2L);
        when(paymentService.checkIdempotencyKey(userId, idempotencyKey)).thenReturn(true);

        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> paymentOrchestrator.processPayment(paymentRequest)
        );

        assertThat(exception.getCode()).isEqualTo("DUPLICATE_PAYMENT");
        assertThat(exception.getMessage()).contains("이미 결제");

    }

}
