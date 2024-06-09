package ru.mts.account.exception;

public class NegativeBalanceException extends CustomException {

    public NegativeBalanceException(String code, String message) {
        super(code, message);
    }
}
