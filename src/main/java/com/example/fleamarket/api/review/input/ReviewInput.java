package com.example.fleamarket.api.review.input;

import lombok.Data;

@Data
public class ReviewInput {
    private String sellId;
    private Boolean asBuyer;
    private Result result;
    private String comment;

    public enum Result {
        GOOD, BAD
    }
}
