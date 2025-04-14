package com.example.fleamarket.api.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BusinessDateGetter {
    public LocalDate getBusinessDate() {
        return LocalDate.now();
    }

    public LocalDateTime getBusinessDateTime() {
        return LocalDateTime.now();
    }
}
