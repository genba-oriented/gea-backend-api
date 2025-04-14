package com.example.fleamarket.api.sell.controller;

import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.input.SellInput;
import com.example.fleamarket.api.sell.service.SellService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellController.class)
class SellControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SellService sellService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void register_validationError() throws Exception {
        SellInput sellInput = new SellInput();

        mockMvc.perform(post("/sell/sells")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sellInput))
        )
            .andExpect(status().isBadRequest());

    }

    @Test
    void register() throws Exception {
        SellInput sellInput = new SellInput();
        sellInput.setProductName("sname01");
        sellInput.setDescription("desc01");
        sellInput.setPrice(999);

        Sell sell = new Sell();
        sell.setId("s99");
        doReturn(sell).when(sellService).register(any(), any());

        mockMvc.perform(post("/sell/sells")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sellInput))
            )
            .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/sell/sells/s99"));

    }

}
