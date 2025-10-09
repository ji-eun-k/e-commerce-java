package kr.hhplus.be.server.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CouponIssueRequest {
    private Long userId;
    private Long couponId;
}
