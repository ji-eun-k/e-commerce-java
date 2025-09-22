package kr.hhplus.be.server.user.interfaces;

import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/v1/user/balance/charge")
    public UserBalance chargeUserBalance(@RequestBody UserBalanceRequest userBalanceRequest){
        return userService.chargeUserBalance(userBalanceRequest);
    }

}
