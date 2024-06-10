package ru.mts.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.account.entity.Account;
import ru.mts.account.model.InfoResponse;
import ru.mts.account.model.UserRequest;
import ru.mts.account.repository.UserRepository;
import ru.mts.account.service.AccountService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    /**
     * Обрабатывает запрос пользователя для предоставления информации о состоянии банковского счета
     *
     * @param accountId идентификатор клиента
     * @return Объект {@link InfoResponse}, содержащий в себе номер телефона клиента и сумма,
     * лежащая на банковском счете
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<InfoResponse> checkAccount(@PathVariable Integer accountId) {
        InfoResponse response = new InfoResponse();

        BigDecimal amount = accountService.checkAccount(accountId);
        response.setPhoneNumber(userRepository.findById(accountId).get().getPhone());
        response.setAmount(amount);

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    /**
     * Обрабатывает запрос пользователя для выполнения операции пополнения счёта в банке
     *
     * @param request запрос, в котором указаны идентификатор клиента и сумма на пополнение
     * @return Объект {@link InfoResponse}, содержащий в себе номер телефона клиента и сумма,
     * лежащая на банковском счете
     */
    @PatchMapping("/refill")
    public ResponseEntity<InfoResponse> refillAccount(@RequestBody UserRequest request) {
        InfoResponse response = new InfoResponse();

        Account account = accountService.refill(request);
        response.setPhoneNumber(userRepository.findById(request.getAccountId()).get().getPhone());
        response.setAmount(account.getAmount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Обрабатывает запрос пользователя для осуществления операции снятия денежных средств с банковского счета
     *
     * @param request запрос, в котором указаны идентификатор клиента и сумма на пополнение
     * @return Объект {@link InfoResponse}, содержащий в себе номер телефона клиента и сумма,
     * лежащая на банковском счете
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<InfoResponse> withdrawAccount(@RequestBody UserRequest request) {
        InfoResponse response = new InfoResponse();

        Account account = accountService.withdraw(request);
        response.setPhoneNumber(userRepository.findById(request.getAccountId()).get().getPhone());
        response.setAmount(account.getAmount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Обрабатывает запрос пользователя для проверки наличия достаточной суммы денег на его банковском счёте
     *
     * @param request запрос, в котором указаны идентификатор клиента и сумма на пополнение
     * @return Объект {@link InfoResponse}, содержащий в себе номер телефона клиента и сумма,
     * лежащая на банковском счете
     */
    @PostMapping("/check")
    public ResponseEntity<Boolean> isEnoughMoney(@RequestBody UserRequest request) {
        Boolean isEnough = accountService.doesHaveEnoughMoney(
                request.getAccountId(),
                request.getMoney()
        );

        return new ResponseEntity<>(isEnough, HttpStatus.OK);
    }
}
