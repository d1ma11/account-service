package ru.mts.account.exception;

public class NoSuchAccountException extends CustomException {

    public NoSuchAccountException(String code, String message) {
        super(code, message);
    }
}
