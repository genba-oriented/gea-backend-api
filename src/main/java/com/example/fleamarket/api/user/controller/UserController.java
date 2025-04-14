package com.example.fleamarket.api.user.controller;

import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.input.UserInput;
import com.example.fleamarket.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PutMapping("/users/{id}/activate")
    public void activate(@PathVariable String id, @RequestBody UserInput userInput, @AuthenticationPrincipal User user) {
        this.userService.activate(user.getId(), userInput);
    }

    @GetMapping("/users/me")
    public User me(@AuthenticationPrincipal User user) {
        return user;
    }


}
