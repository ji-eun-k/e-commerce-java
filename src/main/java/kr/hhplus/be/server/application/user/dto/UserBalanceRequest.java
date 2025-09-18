package kr.hhplus.be.server.application.user.dto;

import kr.hhplus.be.server.domain.user.enumtype.TransactionType;
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
