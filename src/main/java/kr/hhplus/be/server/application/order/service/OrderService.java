package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.dto.OrderProduct;
import kr.hhplus.be.server.application.order.dto.OrderRequest;
import kr.hhplus.be.server.application.order.dto.OrderResponse;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.application.user.service.UserService;
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
        // 유저 검증을 위해 호출
        userService.getUserBalance(orderRequest.getUserId());

        // 상품 가격 가져오기

        // totalPrice 계산

        // 저장

        // 재고 차감
        for (OrderProduct orderProduct: orderRequest.getOrderProducts()) {
            productService.decreaseProductInventory(orderProduct.getProductId(), orderProduct.getQuantity());
        }

        // 추후에 결제 event발행 - eventListner 처리 (@TransactionEventListener)
    }
}
