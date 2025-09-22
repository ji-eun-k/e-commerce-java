package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.dto.ProductSearchRequest;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products")
    public ResponseEntity<Page<Product>> getProducts(@RequestParam ProductSearchRequest request){
        Page<Product> products = productService.getProducts(request);
        return ResponseEntity.ok(products);
    }
}
