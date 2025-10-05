package kr.hhplus.be.server.order;

import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import kr.hhplus.be.server.product.application.dto.ProductOrderDetail;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.config.exception.OrderException;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.application.port.OrderRepository;
import kr.hhplus.be.server.product.application.port.ProductRepository;
import kr.hhplus.be.server.user.application.port.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserPort userPort;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentService paymentService;

    private Long userId;


    @BeforeEach
    public void setup() {
        userId = 1L;
    }

    @Test
    public void 주문_생성_테스트(){
        OrderItem orderItem1 = OrderItem.builder().productId(1L).quantity(2).build();
        OrderItem orderItem2 = OrderItem.builder().productId(3L).quantity(1).build();

        List<ProductOrderDetail> productOrderDetails = new ArrayList<>();
        productOrderDetails.add(ProductOrderDetail.of(orderItem1, BigDecimal.valueOf(10000)));
        productOrderDetails.add(ProductOrderDetail.of(orderItem2, BigDecimal.valueOf(50000)));

        BigDecimal totalPrice = BigDecimal.valueOf(10000).multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(50000));

        OrderRequest orderRequest = OrderRequest.builder().userId(userId).orderItems(List.of(orderItem1, orderItem2)).build();
        ProductOrderResult productOrderResult = ProductOrderResult.of(totalPrice, productOrderDetails);

        when(orderRepository.save(any(Order.class))).thenReturn(1L);
        when(productService.getProductOrderPrice(orderRequest.getOrderItems())).thenReturn(productOrderResult);

        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        assertThat(orderResponse.getOrderId()).isEqualTo(1L);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(totalPrice);

    }

    @Test
    public void 주문_생성_실패_상품정보_없음(){
        OrderRequest orderRequest = OrderRequest.builder().userId(userId).orderItems(List.of()).build();

        OrderException exception = assertThrows(
                OrderException.class, () -> orderService.createOrder(orderRequest)
        );

        assertThat(exception.getCode()).isEqualTo("ORDER_PRODUCT_MISSING");
        assertThat(exception.getMessage()).contains("주문 상품이 없습니다.");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
