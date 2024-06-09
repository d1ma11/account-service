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

    /**
     * Сравнивает количество денег для последующей операции с текущим балансом на счете
     *
     * @param accountId идентификатор счета, с которым идет сравнение
     * @param money     деньги, с которыми нужно провести операцию
     * @return true - если достаточно денег, false - иначе
     */
    @Override
    public boolean doesHaveEnoughMoney(Integer accountId, BigDecimal money) {
        return accountRepository.findById(accountId)
                .map(account -> currentMoneySufficient(account, money))
                .orElse(false);
    }

    /**
     * Операция по пополнению банковского счета
     *
     * @param request запрос, в теле которого содержатся идентификатор счета и сумма денег
     *                на пополнение счета
     * @return банковский счет
     */
    @Override
    public Account topUp(UserRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(request.getAccountId());

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            BigDecimal moneyForTopUp = request.getMoney();
            BigDecimal currentMoney = account.getAmount();
            account.setAmount(currentMoney.add(moneyForTopUp));

            accountRepository.save(account);

            return account;
        }
        throw new NoSuchAccountException(
                "ACCOUNT_NOT_FOUND",
                "Аккаунт не найден со следующим id:" + request.getAccountId());
    }

    /**
     * Операция по снятию денежных средств с банковского счета
     *
     * @param request запрос, в теле которого содержатся идентификатор счета и сумма денег
     *                на снятие
     * @return банковский счет
     */
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
                "Аккаунт не найден со следующим id:" + request.getAccountId());    }

    /**
     * Проверка количества денег на счете
     *
     * @param accountId идентификатор банковского счета
     * @return сумма денежных средств
     */
    @Override
    public BigDecimal checkAccount(Integer accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get().getAmount();
        }
        throw new NoSuchAccountException(
                "ACCOUNT_NOT_FOUND",
                "Аккаунт не найден со следующим id:" + accountId);    }

    private boolean currentMoneySufficient(Account account, BigDecimal money) {
        BigDecimal currentMoney = account.getAmount();
        return currentMoney.compareTo(money) >= 0;
    }
}
