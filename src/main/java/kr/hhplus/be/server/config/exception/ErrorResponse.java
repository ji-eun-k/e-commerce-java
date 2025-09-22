package kr.hhplus.be.server.config.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
