package ru.mts.account.service;

import ru.mts.account.entity.Account;
import ru.mts.account.model.UserRequest;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * Проверка, достаточно ли денег на банковском счете для совершения транзакции
     *
     * @param accountId Идентификатор аккаунта
     * @param money     Сумма транзакции
     * @return true, если достаточно средств, false в противном случае
     */
    boolean doesHaveEnoughMoney(Integer accountId, BigDecimal money);

    /**
     * Пополнение банковского счета определенной суммой денег
     *
     * @param request Запрос на заправку аккаунта
     * @return Обновленная информация о счете после операции
     */
    Account refill(UserRequest request);

    /**
     * Снятие денежных средств с банковского счета
     *
     * @param request Запрос на снятие денег со счета
     * @return Обновленная информация о счете после операции
     */
    Account withdraw(UserRequest request);

    /**
     * Предоставление информации о состоянии банковского счета
     *
     * @param accountId Идентификатор аккаунта
     * @return Баланс аккаунта
     */
    BigDecimal checkAccount(Integer accountId);
}
