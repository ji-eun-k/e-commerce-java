package kr.hhplus.be.server.product;


import kr.hhplus.be.server.order.application.dto.OrderItem;
import kr.hhplus.be.server.product.application.dto.ProductOrderDetail;
import kr.hhplus.be.server.product.application.dto.ProductOrderResult;
import kr.hhplus.be.server.product.application.dto.ProductSearchRequest;
import kr.hhplus.be.server.product.application.service.ProductService;
import kr.hhplus.be.server.config.exception.ProductException;
import kr.hhplus.be.server.product.domain.enumtype.ProductCategory;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductInventory;
import kr.hhplus.be.server.product.application.port.ProductRepository;
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
import static org.mockito.Mockito.verify;
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


    @Test
    public void 상품_재고차감_테스트(){
        ProductInventory productInventory = new ProductInventory(1L, 1);
        when(productRepository.getProductInventory(1L)).thenReturn(productInventory);

        productService.decreaseProductInventory(1L, 1);

        verify(productRepository).saveProductInventory(productInventory);

    }

    @Test
    public void 상품_재고차감_수량부족_실패(){
        ProductInventory productInventory = new ProductInventory(1L, 1);
        when(productRepository.getProductInventory(1L)).thenReturn(productInventory);


        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.decreaseProductInventory(1L, 2)
        );

        assertThat(exception.getCode()).isEqualTo("OUT_OF_STOCK");
        assertThat(exception.getMessage()).contains("재고가 부족");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);

    }

    @Test
    public void 상품_재고차감_잘못된상품수량_실패(){
        ProductInventory productInventory = new ProductInventory(1L, 1);
        when(productRepository.getProductInventory(1L)).thenReturn(productInventory);


        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.decreaseProductInventory(1L, 0)
        );

        assertThat(exception.getCode()).isEqualTo("INVALID_QUANTITY");
        assertThat(exception.getMessage()).contains("상품을 1개 이상");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void 주문상품_가격조회_테스트(){
        OrderItem orderItem1 = OrderItem.builder().productId(1L).quantity(2).build();
        OrderItem orderItem2 = OrderItem.builder().productId(3L).quantity(1).build();
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);

        when(productRepository.getProductPrice(1L)).thenReturn(BigDecimal.valueOf(10000));
        when(productRepository.getProductPrice(3L)).thenReturn(BigDecimal.valueOf(50000));

        ProductOrderDetail product1 = ProductOrderDetail.of(orderItem1, BigDecimal.valueOf(10000));
        ProductOrderDetail product2 = ProductOrderDetail.of(orderItem2, BigDecimal.valueOf(50000));

        ProductOrderResult productOrderDetail = ProductOrderResult.of(BigDecimal.valueOf(70000), List.of(product1, product2));

        ProductOrderResult result = productService.getProductOrderPrice(orderItemList);

        assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(70000));


    }

    @Test
    public void 주문상품_가격조회_상품정보없음_실패(){
        OrderItem orderItem1 = OrderItem.builder().productId(1L).quantity(2).build();
        List<OrderItem> orderItemList = List.of(orderItem1);

        when(productRepository.getProductPrice(1L)).thenReturn(null);

        ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.getProductOrderPrice(orderItemList)
        );

        assertThat(exception.getCode()).isEqualTo("PRODUCT_NOT_FOUND");
        assertThat(exception.getMessage()).contains("존재하지 않는 상품");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);

    }

}


