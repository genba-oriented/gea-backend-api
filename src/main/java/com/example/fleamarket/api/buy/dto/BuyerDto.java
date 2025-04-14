package com.example.fleamarket.api.buy.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyerDto {

    private String userId;
    private String userName;
    private String shippingAddress;
    private LocalDateTime buyDateTime;
}
