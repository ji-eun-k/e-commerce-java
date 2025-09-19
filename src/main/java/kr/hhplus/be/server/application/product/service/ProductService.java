package kr.hhplus.be.server.application.product.service;

import kr.hhplus.be.server.application.product.dto.ProductSearchRequest;
import kr.hhplus.be.server.domain.product.model.Product;
import kr.hhplus.be.server.domain.product.model.ProductInventory;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /*
        상품 조회
     */
    public Page<Product> getProducts(ProductSearchRequest productSearchRequest) {
        productSearchRequest.validation();
        Pageable pageable = PageRequest.of(productSearchRequest.getPageNo(), productSearchRequest.getSize());
        return productRepository.getProducts(productSearchRequest, pageable);
    }


    /*
        상품 재고 차감
     */
    public void decreaseProductInventory(Long productId, int orderQuantity){
        ProductInventory productInventory =  productRepository.getProductInventory(productId);
        productInventory.decreaseProductInventory(orderQuantity);
        productRepository.saveProductInventory(productInventory);

    }
}
