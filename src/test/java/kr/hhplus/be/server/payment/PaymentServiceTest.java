package kr.hhplus.be.server.payment;

import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.dto.PaymentResponse;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.payment.domain.model.Payment;
import kr.hhplus.be.server.payment.application.port.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @Mock
    private PaymentRepository paymentRepository;

    private Long userId;
    private Long orderId;
    private BigDecimal amount;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        orderId = 1L;
        amount = BigDecimal.valueOf(70000);
    }

    @Test
    public void 주문_결제_테스트(){
        PaymentRequest paymentRequest = new PaymentRequest(userId, orderId, amount);
        Payment payment = Payment.from(paymentRequest);
        Payment paymentSetId = Payment.from(paymentRequest);
        paymentSetId.setId(2L);
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentSetId);

        PaymentResponse paymentResponse = PaymentResponse.of(paymentSetId);

        PaymentResponse paymentResult = paymentService.createPayment(paymentRequest);

        assertThat(paymentResult.getOrderId()).isEqualTo(orderId);
        assertThat(paymentResult.getPaymentId()).isEqualTo(2L);

    }

}
