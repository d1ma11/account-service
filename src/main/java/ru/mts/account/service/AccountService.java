package ru.mts.account.service;

import ru.mts.account.entity.Account;
import ru.mts.account.model.UserRequest;

import java.math.BigDecimal;

public interface AccountService {

    boolean doesHaveEnoughMoney(Integer accountId, BigDecimal money);

    Account topUp(UserRequest request);

    Account withdraw(UserRequest request);

    BigDecimal checkAccount(Integer accountId);
}
