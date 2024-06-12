package ru.mts.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mts.account.entity.Account;
import ru.mts.account.exception.NegativeBalanceException;
import ru.mts.account.exception.NoSuchAccountException;
import ru.mts.account.model.UserRequest;
import ru.mts.account.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1);
        account.setAmount(BigDecimal.valueOf(1000));

        userRequest = new UserRequest();
        userRequest.setAccountId(1);
        userRequest.setMoney(BigDecimal.valueOf(500));
    }

    @Test
    void doesHaveEnoughMoney_ShouldReturnTrue_WhenSufficientFunds() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        boolean result = accountService.doesHaveEnoughMoney(1, BigDecimal.valueOf(500));

        assertTrue(result);
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void doesHaveEnoughMoney_ShouldThrowException_WhenAccountNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchAccountException exception = assertThrows(
                NoSuchAccountException.class,
                () -> accountService.doesHaveEnoughMoney(1, BigDecimal.valueOf(500))
        );

        assertEquals("Аккаунт не найден со следующим id: 1", exception.getMessage());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void refill_ShouldIncreaseAccountBalance_WhenAccountExists() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        Account updatedAccount = accountService.refill(userRequest);

        assertEquals(BigDecimal.valueOf(1500), updatedAccount.getAmount());
        verify(accountRepository, times(1)).findById(1);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void refill_ShouldThrowException_WhenAccountNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchAccountException exception = assertThrows(
                NoSuchAccountException.class, () -> accountService.refill(userRequest)
        );

        assertEquals("Аккаунт не найден со следующим id: 1", exception.getMessage());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void withdraw_ShouldDecreaseAccountBalance_WhenSufficientFunds() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        Account updatedAccount = accountService.withdraw(userRequest);

        assertEquals(BigDecimal.valueOf(500), updatedAccount.getAmount());
        verify(accountRepository, times(2)).findById(1);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        userRequest.setMoney(BigDecimal.valueOf(1500));
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        NegativeBalanceException exception = assertThrows(
                NegativeBalanceException.class, () -> accountService.withdraw(userRequest)
        );

        assertEquals(
                "Нельзя снять 1500 с вашего банковского счета: недостаточно средств!",
                exception.getMessage());

        verify(accountRepository, times(2)).findById(1);
    }

    @Test
    void withdraw_ShouldThrowException_WhenAccountNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchAccountException exception = assertThrows(
                NoSuchAccountException.class, () -> accountService.withdraw(userRequest)
        );

        assertEquals("Аккаунт не найден со следующим id: 1", exception.getMessage());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void checkAccount_ShouldReturnAccountBalance_WhenAccountExists() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        BigDecimal balance = accountService.checkAccount(1);

        assertEquals(BigDecimal.valueOf(1000), balance);
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void checkAccount_ShouldThrowException_WhenAccountNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        NoSuchAccountException exception = assertThrows(
                NoSuchAccountException.class, () -> accountService.checkAccount(1)
        );

        assertEquals("Аккаунт не найден со следующим id: 1", exception.getMessage());
        verify(accountRepository, times(1)).findById(1);
    }
}
