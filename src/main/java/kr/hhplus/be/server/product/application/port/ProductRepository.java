package kr.hhplus.be.server.product.application.port;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.product.application.dto.ProductSearchRequest;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;

import java.math.BigDecimal;

public interface ProductRepository {
    Page<Product> getProducts(ProductSearchRequest productSearchRequest, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적락으로 재고 정합성 확보
    ProductInventory getProductInventory(Long productId);

    int saveProductInventory(ProductInventory productInventory);

    BigDecimal getProductPrice(Long productId);
}
