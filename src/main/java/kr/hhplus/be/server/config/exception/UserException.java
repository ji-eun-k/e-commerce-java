package kr.hhplus.be.server.config.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException{
    private final ErrorCode errorCode;

    public UserException(ErrorCode code) {
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
