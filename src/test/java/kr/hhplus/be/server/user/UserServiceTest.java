package kr.hhplus.be.server.user;

import kr.hhplus.be.server.user.application.dto.UserBalanceRequest;
import kr.hhplus.be.server.user.application.service.UserService;
import kr.hhplus.be.server.config.exception.UserException;
import kr.hhplus.be.server.user.domain.model.UserBalance;
import kr.hhplus.be.server.user.application.port.UserPort;
import kr.hhplus.be.server.user.infrastructure.persistence.adapter.UserPersistenceAdapter;
import kr.hhplus.be.server.user.infrastructure.persistence.entity.UserEntity;
import kr.hhplus.be.server.user.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserPort userPort;

    @Mock
    private UserJpaRepository userRepo;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;

    private UserService userService;

    private Long id;
    private BigDecimal balance;
    private BigDecimal amount;

    @BeforeEach
    public void setup() {
        id = 1L;
        balance = BigDecimal.valueOf(200000);
        amount = BigDecimal.valueOf(100000);
    }

    @Test
    public void 잔액_충전_성공(){
        userService = new UserService(userPort);

        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();
        UserBalance userBalance = new UserBalance(id, balance);
        UserBalance afterChargeUserBalance = new UserBalance(id, balance.add( amount));

        when(userPort.getUserBalance(id)).thenReturn(userBalance);
        when(userPort.save(any(UserBalance.class))).thenReturn(1);

        UserBalance userBalanceResult = userService.chargeUserBalance(chargeBalanceRequest);

        assertThat(userBalanceResult.getBalance()).isEqualTo(afterChargeUserBalance.getBalance());
        assertThat(userBalanceResult.getId()).isEqualTo(afterChargeUserBalance.getId());
    }

    @Test
    public void 잔액_충전_실패_사용자없음(){
        userService = new UserService(userPersistenceAdapter);

        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();

        when(userRepo.findById(id)).thenReturn(Optional.empty());

        UserException exception = assertThrows(
                UserException.class,
                () -> userService.chargeUserBalance(chargeBalanceRequest)
        );

        assertThat(exception.getCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(exception.getMessage()).contains("사용자를 찾을 수 없습니다.");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void 잔액_충전_실패_보유가능잔액초과(){
        userService = new UserService(userPersistenceAdapter);

        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount).build();
        UserEntity userEntity = new UserEntity(id, balance.multiply(BigDecimal.valueOf(100)));

        when(userRepo.findById(id)).thenReturn(Optional.of(userEntity));

        UserException exception = assertThrows(
                UserException.class,
                () -> userService.chargeUserBalance(chargeBalanceRequest)
        );

        assertThat(exception.getCode()).isEqualTo("EXCEED_MAX_BALANCE");
        assertThat(exception.getMessage()).contains("최대 보유 가능 잔액은 10,000,000원입니다.");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Test
    public void 잔액_충전_실패_충전금액오류(){
        userService = new UserService(userPersistenceAdapter);

        UserBalanceRequest chargeBalanceRequest = UserBalanceRequest.builder()
                .id(id).amount(amount.multiply(BigDecimal.valueOf(100))).build();
        UserBalance userBalance = new UserBalance(id, balance);

        UserEntity userEntity = new UserEntity(id, balance);

        when(userRepo.findById(id)).thenReturn(Optional.of(userEntity));


        UserException exception = assertThrows(
                UserException.class,
                () -> userService.chargeUserBalance(chargeBalanceRequest)
        );

        assertThat(exception.getCode()).isEqualTo("MAX_CHARGE_AMOUNT");
        assertThat(exception.getMessage()).contains("1회 최대 충전 가능 금액");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
    
    
    @Test
    public void 잔액_사용_성공(){

        userService = new UserService(userPort);
        UserBalanceRequest userBalanceRequest = UserBalanceRequest.builder().id(id).amount(amount).build();

        UserBalance userBalance = new UserBalance(id, balance);
        UserBalance afterUseUserBalance = new UserBalance(id, balance.subtract( amount));

        when(userPort.getUserBalance(id)).thenReturn(userBalance);
        when(userPort.save(any(UserBalance.class))).thenReturn(1);

        UserBalance userBalanceResult = userService.useUserBalance(userBalanceRequest);

        assertThat(userBalanceResult.getBalance()).isEqualTo(afterUseUserBalance.getBalance());
        assertThat(userBalanceResult.getId()).isEqualTo(afterUseUserBalance.getId());
    }

    @Test
    public void 잔액_사용_실패_사용금액검증(){

        userService = new UserService(userPersistenceAdapter);

        UserBalanceRequest userBalanceRequest = UserBalanceRequest.builder().id(id).amount(BigDecimal.ZERO).build();
        UserEntity userEntity = new UserEntity(id, balance);

        when(userRepo.findById(id)).thenReturn(Optional.of(userEntity));


        UserException exception = assertThrows(
                UserException.class,
                () -> userService.useUserBalance(userBalanceRequest)
        );

        assertThat(exception.getCode()).isEqualTo("INVALID_USE_AMOUNT");
        assertThat(exception.getMessage()).contains("유효하지 않은 사용 금액");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void 잔액_사용_실패_잔액검증(){
        userService = new UserService(userPersistenceAdapter);

        UserBalanceRequest userBalanceRequest = UserBalanceRequest.builder().id(id).amount(amount).build();
        UserEntity userEntity = new UserEntity(id, BigDecimal.ZERO);

        when(userRepo.findById(id)).thenReturn(Optional.of(userEntity));


        UserException exception = assertThrows(
                UserException.class,
                () -> userService.useUserBalance(userBalanceRequest)
        );

        assertThat(exception.getCode()).isEqualTo("INSUFFICIENT_BALANCE");
        assertThat(exception.getMessage()).contains("잔액이 부족");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
    }


}
