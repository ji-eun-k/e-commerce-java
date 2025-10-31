package kr.hhplus.be.server.order.interfaces;

import kr.hhplus.be.server.common.OrderOrchestrator;
import kr.hhplus.be.server.order.application.dto.OrderRequest;
import kr.hhplus.be.server.order.application.dto.OrderResponse;
import kr.hhplus.be.server.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class OrderController {
    private final OrderOrchestrator orchestrator;

    @PostMapping("/api/v1/orders")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest){
        return orchestrator.processOrder(orderRequest);
    }
}
