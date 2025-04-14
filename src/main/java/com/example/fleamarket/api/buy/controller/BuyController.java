package com.example.fleamarket.api.buy.controller;

import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.exception.AlreadyBoughtException;
import com.example.fleamarket.api.buy.exception.BalanceShortageException;
import com.example.fleamarket.api.buy.exception.SellByMyselfException;
import com.example.fleamarket.api.buy.input.BuyInput;
import com.example.fleamarket.api.buy.service.BuyService;
import com.example.fleamarket.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buy/buys")
@RequiredArgsConstructor
public class BuyController {

    private final BuyService buyService;

    @PostMapping
    public ResponseEntity<Void> buy(@RequestBody BuyInput buyInput, @AuthenticationPrincipal User user) {
        Buy buy = this.buyService.buy(buyInput, user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(buy.getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping
    public List<Buy> getAll(@RequestParam(required = false) String sellId, @AuthenticationPrincipal User user) {
        if (sellId == null) {
            return this.buyService.getByUserId(user.getId());
        } else {
            return List.of(this.buyService.getBySellIdAndUserId(sellId, user.getId()));
        }

    }



    @GetMapping("/{id}")
    public Buy getById(@PathVariable String id, @AuthenticationPrincipal User user) {
        return this.buyService.getByIdAndUserId(id, user.getId());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleAlreadyBoughtException(AlreadyBoughtException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("cause", "AlreadyBought");
        return map;
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBalanceShortageException(BalanceShortageException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("cause", "BalanceShortage");
        return map;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleSellByMyselfException(SellByMyselfException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("cause", "SellByMyself");
        return map;
    }

}
