package com.example.fleamarket.api.shipping.controller;

import com.example.fleamarket.api.shipping.exception.AlreadyShippedException;
import com.example.fleamarket.api.shipping.service.ShippingService;
import com.example.fleamarket.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PutMapping("/sells/{id}/shipped")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void shipped(@PathVariable String id, @AuthenticationPrincipal User user) {
        this.shippingService.shipped(id, user.getId());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleAlreadyShippedException(AlreadyShippedException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("cause", "AlreadyShipped");
        return map;
    }


}
