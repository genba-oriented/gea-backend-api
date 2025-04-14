package com.example.fleamarket.api.buy.controller;

import com.example.fleamarket.api.buy.dto.BuyerDto;
import com.example.fleamarket.api.buy.service.BuyForSellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyForSellerController.class)
@AutoConfigureRestDocs
class BuyForSellerControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BuyForSellerService buyForSellerService;

    @Test
    void getBuyer() throws Exception {
        BuyerDto dto = new BuyerDto();
        dto.setUserId("u01");
        dto.setUserName("uname01");
        dto.setShippingAddress("address01");
        dto.setBuyDateTime(LocalDateTime.of(2025, 3, 4, 20, 20, 20));
        doReturn(dto).when(this.buyForSellerService).getBuyerBySellIdAndSellerId(any(), any());

        this.mockMvc.perform(get("/buy/for-seller/buyers")
                .param("sellId", "s01")
            )
            .andExpect(status().isOk())
            .andDo(document("buy/for-seller/getBuyerInfo",
                queryParameters(
                    parameterWithName("sellId").description("出品ID")
                )
            ));


    }
}
