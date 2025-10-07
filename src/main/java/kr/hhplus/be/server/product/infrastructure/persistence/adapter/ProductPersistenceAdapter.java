package kr.hhplus.be.server.product.infrastructure.persistence.adapter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.config.exception.ErrorCode;
import kr.hhplus.be.server.config.exception.ProductException;
import kr.hhplus.be.server.product.application.dto.ProductSearchRequest;
import kr.hhplus.be.server.product.application.port.ProductPort;
import kr.hhplus.be.server.product.domain.model.Product;
import kr.hhplus.be.server.product.domain.model.ProductInventory;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductEntity;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.ProductInventoryEntity;
import kr.hhplus.be.server.product.infrastructure.persistence.entity.QProductEntity;
import kr.hhplus.be.server.product.infrastructure.persistence.repository.ProductInventoryJpaRepository;
import kr.hhplus.be.server.product.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductInventoryJpaRepository productInventoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    // 상품 목록 페이지 조회
    @Override
    public Page<Product> getProducts(ProductSearchRequest productSearchRequest, Pageable pageable) {
        QProductEntity qProductEntity = QProductEntity.productEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if(!ObjectUtils.isEmpty(productSearchRequest.getCategory())){
            builder.and(qProductEntity.category.eq(productSearchRequest.getCategory()));
        }

        if(!ObjectUtils.isEmpty(productSearchRequest.getMinPrice())){
            builder.and(qProductEntity.price.goe(productSearchRequest.getMinPrice()));
        }

        if(!ObjectUtils.isEmpty(productSearchRequest.getMaxPrice())){
            builder.and(qProductEntity.price.loe(productSearchRequest.getMaxPrice()));
        }

        // 페이징 데이터 조회
        List<ProductEntity> productEntities = queryFactory
                .selectFrom(qProductEntity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Product> products = productEntities.stream().map(this::toProductDomain).collect(Collectors.toList());

        // 전체 데이터 개수 조회
        long total = queryFactory
                .selectFrom(qProductEntity)
                .where(builder)
                .fetch().size();

        return new PageImpl<>(products, pageable, total);
    }

    // 상품 재고 조회 (비관적 락)
    @Override
    public ProductInventory getProductInventory(Long productId) {
        ProductInventoryEntity productInventoryEntity = productInventoryJpaRepository.findByProductId(productId).orElseThrow(() -> new ProductException(ErrorCode.OUT_OF_STOCK));

        return toProductInventoryDomain(productInventoryEntity);
    }

    // 상품 재고 저장
    @Override
    public ProductInventory saveProductInventory(ProductInventory productInventory) {
        ProductInventoryEntity entity = productInventoryJpaRepository.save(toProductInventoryEntity(productInventory));
        return toProductInventoryDomain(entity);
    }

    // 상품 가격 조회
    @Override
    public BigDecimal getProductPrice(Long productId) {
        QProductEntity qProductEntity = QProductEntity.productEntity;
        return queryFactory
                .select(qProductEntity.price)
                .from(qProductEntity)
                .where(qProductEntity.id.eq(productId))
                .fetchOne();
    }

    private Product toProductDomain(ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getCategory(), productEntity.getProductName(), productEntity.getPrice());
    }

    private ProductInventory toProductInventoryDomain(ProductInventoryEntity productInventoryEntity) {
        return new ProductInventory(productInventoryEntity.getProductId(), productInventoryEntity.getInventory());
    }

    private ProductInventoryEntity toProductInventoryEntity(ProductInventory productInventory) {
        return new ProductInventoryEntity(productInventory.getProductId(), productInventory.getInventory());
    }
}
