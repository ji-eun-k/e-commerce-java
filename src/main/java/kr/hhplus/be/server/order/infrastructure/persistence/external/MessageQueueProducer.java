package kr.hhplus.be.server.order.infrastructure.persistence.external;

import kr.hhplus.be.server.order.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueProducer {

    public void sendMessage(Long orderId) {
        // TODO : 외부 주문 메시지 전송
    }
}
