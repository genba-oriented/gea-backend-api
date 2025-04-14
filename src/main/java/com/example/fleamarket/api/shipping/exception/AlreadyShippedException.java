package com.example.fleamarket.api.shipping.exception;

public class AlreadyShippedException extends RuntimeException {
    public AlreadyShippedException(String msg) {
        super(msg);
    }
}
