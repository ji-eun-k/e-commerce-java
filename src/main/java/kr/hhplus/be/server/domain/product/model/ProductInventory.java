package kr.hhplus.be.server.domain.product.model;

import kr.hhplus.be.server.domain.exception.ErrorCode;
import kr.hhplus.be.server.domain.exception.ProductException;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ProductInventory {
    private Long productId;
    private int inventory;

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
