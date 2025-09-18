package kr.hhplus.be.server.application.product.service;

import kr.hhplus.be.server.application.product.dto.ProductSearchRequest;
import kr.hhplus.be.server.domain.product.model.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getProducts(ProductSearchRequest productSearchRequest) {
        productSearchRequest.validate();
        Pageable pageable = PageRequest.of(productSearchRequest.getPageNo(), productSearchRequest.getSize());
        return productRepository.getProducts(productSearchRequest, pageable);
    }
}
