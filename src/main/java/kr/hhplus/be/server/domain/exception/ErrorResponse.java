package kr.hhplus.be.server.domain.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
