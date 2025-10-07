package kr.hhplus.be.server.product.domain.model;

import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.ProductException;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ProductInventory {
    private Long productId;
    private int inventory;

    public static ProductInventory empty(long productId){
        return new ProductInventory(productId, 0);
    }

    public void decreaseProductInventory(int orderQuantity){
        validation(orderQuantity, inventory);
        inventory -= orderQuantity;
    }

    private void validation(int orderQuantity, int inventory){
        if(orderQuantity > inventory){
            throw new ProductException(ErrorCode.OUT_OF_STOCK);
        }
        if(orderQuantity < 1){
            throw new ProductException(ErrorCode.INVALID_QUANTITY);
        }
    }
}
