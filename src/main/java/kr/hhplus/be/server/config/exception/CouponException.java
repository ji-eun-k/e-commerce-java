package kr.hhplus.be.server.config.exception;

import org.springframework.http.HttpStatus;

public class CouponException extends RuntimeException {

    private final ErrorCode errorCode;

    public CouponException(ErrorCode code) {
        super(code.getMessage());
        this.errorCode = code;
    }

    public String getCode(){
        return errorCode.getCode();
    }

    public HttpStatus getHttpStatus(){
        return errorCode.getHttpStatus();
    }
}
