package kr.hhplus.be.server.product.application.service;

import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.product.application.dto.ProductOrderDetail;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.product.application.dto.ProductSearchRequest;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductInventory;
import kr.hhplus.be.server.product.application.port.ProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductPort productPort;

    /*
        상품 조회
     */
    public Page<Product> getProducts(ProductSearchRequest productSearchRequest) {
        productSearchRequest.validation();
        Pageable pageable = PageRequest.of(productSearchRequest.getPageNo(), productSearchRequest.getSize());
        return productPort.getProducts(productSearchRequest, pageable);
    }


    /*
        상품 재고 차감
     */
    @Transactional
    public ProductInventory decreaseProductInventory(Long productId, int orderQuantity){
        ProductInventory productInventory =  productPort.getProductInventory(productId);
        productInventory.decreaseProductInventory(orderQuantity);
        return productPort.saveProductInventory(productInventory);
    }

    /*
     상품 가격 조회
     */
    public ProductOrderResult getProductOrderPrice(List<OrderItem> orderItems) {
        List<ProductOrderDetail> productOrderDetails = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItems) {
            BigDecimal productPrice = productPort.getProductPrice(orderItem.getProductId());
            productOrderDetails.add(ProductOrderDetail.of(orderItem, productPrice));
            totalPrice = totalPrice.add(productPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }

        return ProductOrderResult.of(totalPrice, productOrderDetails);

    }
}
