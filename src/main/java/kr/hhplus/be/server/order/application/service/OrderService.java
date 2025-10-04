package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.payment.application.dto.PaymentRequest;
import kr.hhplus.be.server.payment.application.service.PaymentService;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.application.port.OrderRepository;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private UserService userService;
    private ProductService productService;
    private PaymentService paymentService;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest){
        orderRequest.validation();
        // 유저 검증을 위해 호출
        UserBalance userBalance = userService.getUserBalance(orderRequest.getUserId());

        // totalPrice 계산가져오기
        ProductOrderResult productOrderResult = productService.getProductOrderPrice(orderRequest.getOrderItems());

        // 저장
        Order order = Order.of(orderRequest, productOrderResult);
        Long orderId = orderRepository.save(order);

        // 재고 차감
        for (OrderItem orderItem : orderRequest.getOrderItems()) {
            productService.decreaseProductInventory(orderItem.getProductId(), orderItem.getQuantity());
        }

        // 추후에 결제 event발행 - eventListner 처리 (@TransactionEventListener)
        paymentService.createPayment(PaymentRequest.of(order));

        return OrderResponse.from(orderId, order);
    }

    /* 주문 취소 - 화면에서 주문 취소 OR 결제 실패 시 호출 */
    @Transactional
    public void cancelOrder(Long orderId){

    }
}
