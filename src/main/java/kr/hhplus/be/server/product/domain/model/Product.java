package kr.hhplus.be.server.product.domain.model;

import kr.hhplus.be.server.product.domain.enumtype.ProductCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Product {
    private Long id;
    private ProductCategory category;
    private String productName;
    private BigDecimal price;
}
