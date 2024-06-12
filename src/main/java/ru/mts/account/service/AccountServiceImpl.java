package ru.mts.account.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mts.account.entity.Account;
import ru.mts.account.exception.NegativeBalanceException;
import ru.mts.account.exception.NoSuchAccountException;
import ru.mts.account.model.UserRequest;
import ru.mts.account.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public boolean doesHaveEnoughMoney(Integer accountId, BigDecimal money) {
        return accountRepository.findById(accountId)
                .map(account -> currentMoneySufficient(account, money))
                .orElseThrow(() -> new NoSuchAccountException(
                        "ACCOUNT_NOT_FOUND",
                        "Аккаунт не найден со следующим id: " + accountId));
    }

    @Override
    public Account refill(UserRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getAccountId());

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            BigDecimal moneyForRefill = request.getMoney();
            BigDecimal currentMoney = account.getAmount();
            account.setAmount(currentMoney.add(moneyForRefill));

            accountRepository.save(account);

            return account;
        }
        throw new NoSuchAccountException(
                "ACCOUNT_NOT_FOUND",
                "Аккаунт не найден со следующим id: " + request.getAccountId());
    }

    @Override
    public Account withdraw(UserRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getAccountId());

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            BigDecimal withdrawMoney = request.getMoney();
            BigDecimal currentMoney = account.getAmount();

            if (doesHaveEnoughMoney(request.getAccountId(), withdrawMoney)) {
                account.setAmount(currentMoney.subtract(withdrawMoney));
                accountRepository.save(account);
                return account;
            }
            throw new NegativeBalanceException(
                    "NEGATIVE_BALANCE",
                    "Нельзя снять " + withdrawMoney + " с вашего банковского счета: недостаточно средств!");

        }
        throw new NoSuchAccountException(
                "ACCOUNT_NOT_FOUND",
                "Аккаунт не найден со следующим id: " + request.getAccountId());
    }

    @Override
    public BigDecimal checkAccount(Integer accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get().getAmount();
        }
        throw new NoSuchAccountException(
                "ACCOUNT_NOT_FOUND",
                "Аккаунт не найден со следующим id: " + accountId);
    }

    private boolean currentMoneySufficient(Account account, BigDecimal money) {
        BigDecimal currentMoney = account.getAmount();
        return currentMoney.compareTo(money) >= 0;
    }
}
