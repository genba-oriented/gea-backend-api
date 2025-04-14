package com.example.fleamarket.api.review.exception;

public class AlreadyReviewedException extends RuntimeException {
    public AlreadyReviewedException(String msg) {
        super(msg);
    }
}
