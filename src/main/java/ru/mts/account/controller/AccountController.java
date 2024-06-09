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

    @GetMapping("/{accountId}")
    public ResponseEntity<InfoResponse> checkAccount(@PathVariable Integer accountId) {
        InfoResponse response = new InfoResponse();

        BigDecimal amount = accountService.checkAccount(accountId);
        response.setPhoneNumber(userRepository.findById(accountId).get().getPhone());
        response.setAmount(amount);

        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PatchMapping("/topUp")
    public ResponseEntity<InfoResponse> topUpMoney(@RequestBody UserRequest request) {
        InfoResponse response = new InfoResponse();

        Account account = accountService.topUp(request);
        response.setPhoneNumber(userRepository.findById(request.getAccountId()).get().getPhone());
        response.setAmount(account.getAmount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<InfoResponse> withdrawMoney(@RequestBody UserRequest request) {
        InfoResponse response = new InfoResponse();

        Account account = accountService.withdraw(request);
        response.setPhoneNumber(userRepository.findById(request.getAccountId()).get().getPhone());
        response.setAmount(account.getAmount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> isEnoughMoney(@RequestBody UserRequest request) {
        Boolean isEnough = accountService.doesHaveEnoughMoney(
                request.getAccountId(),
                request.getMoney()
        );

        return new ResponseEntity<>(isEnough, HttpStatus.OK);
    }
}
