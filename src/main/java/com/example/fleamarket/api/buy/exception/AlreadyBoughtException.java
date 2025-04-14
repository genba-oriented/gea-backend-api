package com.example.fleamarket.api.buy.exception;

public class AlreadyBoughtException extends RuntimeException {
    public AlreadyBoughtException(String msg) {
        super(msg);
    }
}
