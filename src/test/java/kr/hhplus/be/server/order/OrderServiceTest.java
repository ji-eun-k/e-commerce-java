package kr.hhplus.be.server.order;

import kr.hhplus.be.server.application.order.dto.OrderProduct;
import kr.hhplus.be.server.application.order.dto.OrderRequest;
import kr.hhplus.be.server.application.order.dto.OrderResponse;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.product.model.ProductInventory;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.user.model.UserBalance;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @InjectMocks
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    private Long userId;


    @BeforeEach
    public void setup() {
        userId = 1L;
    }

    @Test
    public void 주문_생성_테스트(){
        OrderProduct orderProduct1 = OrderProduct.builder().productId(1L).quantity(2).price(BigDecimal.valueOf(10000)).build();
        OrderProduct orderProduct2 = OrderProduct.builder().productId(3L).quantity(1).price(BigDecimal.valueOf(54000)).build();

        BigDecimal totalPrice = BigDecimal.valueOf(10000).multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(54000));

        OrderRequest orderRequest = OrderRequest.builder().userId(userId).orderProducts(List.of(orderProduct1, orderProduct2)).build();

        when(userRepository.getUserBalance(userId)).thenReturn(new UserBalance(userId, BigDecimal.valueOf(64000)));
        // TODO : DB에 저장된 상품 가격 가져와서 매핑하고, 총 결제 금액 계산
        when(productRepository.getProductPrice(1L)).thenReturn(BigDecimal.valueOf(10000));
        when(productRepository.getProductPrice(3L)).thenReturn(BigDecimal.valueOf(54000));
        when(orderRepository.saveOrder(any(Order.class))).thenReturn(1L);
        when(productRepository.getProductInventory(1L)).thenReturn(ProductInventory.builder().productId(1L).inventory(5).build());
        when(productRepository.getProductInventory(3L)).thenReturn(ProductInventory.builder().productId(3L).inventory(5).build());


        OrderResponse orderResponse = orderService.createOrder(orderRequest);


        assertThat(orderResponse.getOrderId()).isEqualTo(1L);
        assertThat(orderResponse.getFinalPrice()).isEqualTo(totalPrice);

    }
}
