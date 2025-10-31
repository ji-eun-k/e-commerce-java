package kr.hhplus.be.server.user.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
public class UserBalanceRequest {
    private Long id;
    private BigDecimal amount;
}
