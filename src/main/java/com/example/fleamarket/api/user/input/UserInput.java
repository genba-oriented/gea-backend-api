package com.example.fleamarket.api.user.input;

import lombok.Data;

@Data
public class UserInput {
    private String name;
    private String shippingAddress;
    private Integer deposit;
}
