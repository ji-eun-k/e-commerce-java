package kr.hhplus.be.server.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MIN_PRICE("MIN_PRICE", "조회 최소 금액은 0보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST),
    MAX_PRICE("MAX_PRICE", "최대 조회 가능 금액을 초과했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY("INVALID_CATEGORY", "유효하지 않은 카테고리입니다.", HttpStatus.BAD_REQUEST),
    MISSING_PAGE_PARAMETER("MISSING_PAGE_PARAMETER", "필수 페이지 파라미터가 누락되었습니다", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COUPON_NOT_FOUND("COUPON_NOT_FOUND", "존재하지 않는 쿠폰 번호입니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "존재하지 않는 상품입니다.",  HttpStatus.NOT_FOUND),


    OUT_OF_STOCK("OUT_OF_STOCK", "상품 재고가 부족합니다.", HttpStatus.CONFLICT),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "잔액이 부족합니다.", HttpStatus.CONFLICT),
    DUPLICATE_PAYMENT("DUPLICATE_PAYMENT", "이미 결제가 완료되었습니다.", HttpStatus.CONFLICT),


    ORDER_PRODUCT_MISSING("ORDER_PRODUCT_MISSING", "주문 상품이 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("INVALID_QUANTITY", "상품을 1개 이상 주문해주세요.", HttpStatus.BAD_REQUEST),



    INVALID_CHARGE_AMOUNT("INVALID_CHARGE_AMOUNT", "유효하지 않은 충전 금액입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USE_AMOUNT("INVALID_USE_AMOUNT", "유효하지 않은 사용 금액입니다.", HttpStatus.BAD_REQUEST),
    MIN_CHARGE_AMOUNT("MIN_CHARGE_AMOUNT", "1회 최소 충전 가능 금액은 1,000원입니다.", HttpStatus.BAD_REQUEST),
    MAX_CHARGE_AMOUNT("MAX_CHARGE_AMOUNT", "1회 최대 충전 가능 금액은 5,000,000원입니다.", HttpStatus.BAD_REQUEST),
    EXCEED_MAX_BALANCE("EXCEED_MAX_BALANCE", "최대 보유 가능 잔액은 10,000,000원입니다.", HttpStatus.UNPROCESSABLE_ENTITY), // 클라이언트가 올바른 요청을 보냈지만, 서버가 요청을 처리할 수 없음 (서버 내부 정책에 의하여 처리 불가)
    CHARGE_BALANCE_FAILED("CHARGE_BALANCE_FAILED", "잔액 충전이 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    SEARCH_DATE_ERROR("SEARCH_DATE_ERROR", "검색 시작 일자는 검색 종료 일자보다 클 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_TYPE_ERROR("ORDER_TYPE_ERROR", "유효하지 않은 정렬 방법입니다.", HttpStatus.BAD_REQUEST),
    TRANSACTION_TYPE_ERROR("TRANSACTION_TYPE_ERROR", "유효하지 않은 트랜잭션 타입입니다.",  HttpStatus.BAD_REQUEST),
    INVALID_STATUS("INVALID_STATUS", "유효하지 않은 상태 조건 입니다.", HttpStatus.BAD_REQUEST),


    EXCEED_COUPON_LIMIT("EXCEED_COUPON_LIMIT", "준비된 쿠폰 수량이 모두 소진되었습니다.", HttpStatus.CONFLICT),
    EXCEED_MAX_COUPON("EXCEED_MAX_COUPON", "최대 보유 가능한 쿠폰 매수를 초과했습니다.", HttpStatus.UNPROCESSABLE_ENTITY), // 클라이언트가 올바른 요청을 보냈지만, 서버가 요청을 처리할 수 없음 (서버 내부 정책에 의하여 처리 불가)
    COUPON_DOWNLOAD_TIME("COUPON_DOWNLOAD_TIME", "쿠폰 다운로드 시간이 아닙니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
