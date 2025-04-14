package com.example.fleamarket.api.review.dto;

import lombok.Data;

@Data
public class RatedUserDto {
    private String userId;
    private String userName;
    private Integer reviewCount;
    private Double averageScore;
}
