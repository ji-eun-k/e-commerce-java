package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.application.product.dto.ProductSearchRequest;
import kr.hhplus.be.server.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    Page<Product> getProducts(ProductSearchRequest productSearchRequest, Pageable pageable);
}
