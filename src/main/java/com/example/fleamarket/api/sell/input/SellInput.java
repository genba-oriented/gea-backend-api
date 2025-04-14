package com.example.fleamarket.api.sell.input;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SellInput {
    @NotBlank
    @Size(max = 50)
    private String productName;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    @Min(100)
    @Max(99999)
    private Integer price;
}
