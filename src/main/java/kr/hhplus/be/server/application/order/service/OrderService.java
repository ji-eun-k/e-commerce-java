package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.dto.OrderItem;
import kr.hhplus.be.server.application.order.dto.OrderRequest;
import kr.hhplus.be.server.application.order.dto.OrderResponse;
import kr.hhplus.be.server.application.product.dto.ProductOrderResult;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.application.user.service.UserService;
import kr.hhplus.be.server.domain.order.model.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.user.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private UserService userService;
    private ProductService productService;

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

        return OrderResponse.from(orderId, order);
    }
}
