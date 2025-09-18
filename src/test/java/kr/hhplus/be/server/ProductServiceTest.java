package kr.hhplus.be.server;


import kr.hhplus.be.server.application.product.dto.ProductSearchRequest;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.exception.ProductException;
import kr.hhplus.be.server.domain.product.enumtype.ProductCategory;
import kr.hhplus.be.server.domain.product.model.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Integer size;
    private Integer pageNo;
    private LocalDateTime fixTime;
    Page<Product> mockProducts;
    @BeforeEach
    public void setup() {
        size = 5;
        pageNo = 0;
        fixTime = LocalDateTime.now();

        List<Product> productList = List.of(new Product(1L, ProductCategory.FASHION, "바지", BigDecimal.valueOf(50000), fixTime, fixTime),
                new Product(2L, ProductCategory.ELECTRONICS, "게이밍 노트북", BigDecimal.valueOf(1450000), fixTime, fixTime));
        Pageable pageable = PageRequest.of(pageNo, size);
        mockProducts = new PageImpl<>(productList, pageable, productList.size());
    }

    @Test
    public void 상품_목록_조회_성공(){

        // given
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder().category(ProductCategory.ALL).size(size).pageNo(pageNo).build();

        when(productService.getProducts(productSearchRequest)).thenReturn(mockProducts);

        // when
        Page<Product> products = productService.getProducts(productSearchRequest);

        // then
        assertThat(products.getSize()).isEqualTo(size);
        assertThat(products.getNumber()).isEqualTo(pageNo);
    }

    @Test
    public void 상품_목록_금액조건조회_실패(){
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder()
                .minPrice(BigDecimal.valueOf(-1)).maxPrice(BigDecimal.valueOf(3000)).size(size).pageNo(pageNo).build();

        // when
        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.getProducts(productSearchRequest)
        );

        assertThat(exception.getCode()).isEqualTo("MIN_PRICE");
        assertThat(exception.getMessage()).contains("0보다 작을 수 없습니다.");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 상품_목록_페이징조회예외_검증(){
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder()
                .minPrice(BigDecimal.valueOf(0)).maxPrice(BigDecimal.valueOf(3000)).build();

        // when
        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.getProducts(productSearchRequest)
        );

        assertThat(exception.getCode()).isEqualTo("MISSING_PAGE_PARAMETER");
        assertThat(exception.getMessage()).contains("누락");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}


