package com.example.fleamarket.api.buy.controller;

import com.example.fleamarket.api.EnableSecurityTest;
import com.example.fleamarket.api.buy.service.BuyService;
import com.example.fleamarket.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyController.class)
@EnableSecurityTest
class BuyControllerSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BuyService buyService;

    @MockitoBean
    UserService userService;

    @Test
    void notAuthorized() throws Exception {
        this.mockMvc.perform(get("/buy/buys")).andExpect(status().isUnauthorized());
    }


}
