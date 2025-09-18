package kr.hhplus.be.server.application.product.dto;

import kr.hhplus.be.server.domain.exception.ErrorCode;
import kr.hhplus.be.server.domain.exception.ProductException;
import kr.hhplus.be.server.domain.product.enumtype.ProductCategory;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductSearchRequest {
    /* TODO : Enum 타입 검증 추가 */
    private ProductCategory category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer size;
    private Integer pageNo;

    public void validate() {
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException(ErrorCode.MIN_PRICE);
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.valueOf(100000000)) > 0){
            throw new ProductException(ErrorCode.MAX_PRICE);
        }
        if (ObjectUtils.isEmpty(size) || ObjectUtils.isEmpty(pageNo)) {
            throw new ProductException(ErrorCode.MISSING_PAGE_PARAMETER);
        }
    }
}
