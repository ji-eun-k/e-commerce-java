package kr.hhplus.be.server.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error", e.getMessage()));
    }

    @ExceptionHandler(value = ProductException.class)
    public ResponseEntity<ErrorResponse> handleException(ProductException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getCode(), e.getMessage()));
    }
}
