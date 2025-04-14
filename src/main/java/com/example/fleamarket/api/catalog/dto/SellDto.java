package com.example.fleamarket.api.catalog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SellDto {
     private String id;
     private String userId;
     private String productName;
     private String description;
     private Integer price;
     private LocalDateTime sellDateTime;
     private Boolean sold;
     private List<String> productImageIds;
}
