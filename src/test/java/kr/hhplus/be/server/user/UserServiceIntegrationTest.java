package kr.hhplus.be.server.user;

import kr.hhplus.be.server.ServerApplication;
import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.port.UserPort;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ServerApplication.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserPort userPort;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private UserService userService;

    private Long id;
    private String name;
    private BigDecimal balance;
    private BigDecimal amount;


    @BeforeEach
    public void setUp() {
        name = "name";
        balance = BigDecimal.valueOf(200000);
        amount = BigDecimal.valueOf(100000);
        userService = new UserService(userPort);

        // 테스트 전 미리 데이터 저장
        UserEntity userEntity = new UserEntity(null, name, balance);
        userEntity = userJpaRepository.save(userEntity);

        id = userEntity.getId();

    }

    @Test
    public void 잔액_충전_통합테스트() {
        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();

        UserBalance userBalance = userService.chargeUserBalance(chargeBalanceRequest);
        assertThat(userBalance).isNotNull();
        assertThat(userBalance.getBalance().compareTo(balance.add(amount))).isEqualTo(0);
        assertThat(userBalance.getId()).isEqualTo(id);
    }
}
