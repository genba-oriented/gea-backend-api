package com.example.fleamarket.api.buy.controller;

import com.example.fleamarket.api.buy.dto.BuyerDto;
import com.example.fleamarket.api.buy.service.BuyForSellerService;
import com.example.fleamarket.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buy/for-seller")
@RequiredArgsConstructor
public class BuyForSellerController {

    private final BuyForSellerService buyForSellerService;
    @GetMapping("/buyers")
    public BuyerDto getBuyer(@RequestParam String sellId, @AuthenticationPrincipal User user) {
        return this.buyForSellerService.getBuyerBySellIdAndSellerId(sellId, user.getId());
    }

}
