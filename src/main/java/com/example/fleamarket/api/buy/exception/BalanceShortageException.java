package com.example.fleamarket.api.buy.exception;

public class BalanceShortageException extends RuntimeException {
    public BalanceShortageException(String msg) {
        super(msg);
    }
}
