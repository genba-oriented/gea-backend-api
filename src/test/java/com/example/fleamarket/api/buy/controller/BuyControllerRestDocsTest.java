package com.example.fleamarket.api.buy.controller;

import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.exception.AlreadyBoughtException;
import com.example.fleamarket.api.buy.input.BuyInput;
import com.example.fleamarket.api.buy.service.BuyService;
import com.example.fleamarket.api.sell.entity.ProductImage;
import com.example.fleamarket.api.sell.entity.Sell;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyController.class)
@AutoConfigureRestDocs
class BuyControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BuyService buyService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void buy() throws Exception {
        Buy buy = new Buy();
        buy.setId("b99");
        doReturn(buy).when(this.buyService).buy(any(), any());

        BuyInput buyInput = new BuyInput();
        buyInput.setSellId("s01");
        this.mockMvc.perform(post("/buy/buys")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(buyInput))
        )
            .andExpect(status().isCreated())
            .andDo(document("buy/buy",
                requestFields(
                    fieldWithPath("sellId").description("出品ID")
                ),
                responseHeaders(
                    headerWithName("Location").description("作成された購入データのURL"))
            ));

    }

    @Test
    void buy_alreadyBought() throws Exception {
        doThrow(new AlreadyBoughtException("")).when(this.buyService).buy(any(), any());

        BuyInput buyInput = new BuyInput();
        buyInput.setSellId("s01");
        this.mockMvc.perform(post("/buy/buys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(buyInput))
            )
            .andExpect(status().isBadRequest())
            .andDo(document("buy/buy_alreadyBought",
                responseFields(
                    fieldWithPath("cause").description("エラー原因 AlreadyBought:すでに購入されている BalanceShortage:残高が不足している")
                )
            ));

    }

    @Test
    void getAll() throws Exception {
        List<Buy> buys = new ArrayList<>();
        buys.add(createBuy(1));
        buys.add(createBuy(2));

        doReturn(buys).when(this.buyService).getByUserId(any());
        this.mockMvc.perform(get("/buy/buys"))
            .andExpect(status().isOk())
            .andDo(document("buy/getAll"));
    }
    @Test
    void getAll_filterBySellId() throws Exception {
        doReturn(createBuy(1)).when(this.buyService).getBySellIdAndUserId(any(), any());
        this.mockMvc.perform(get("/buy/buys")
                .param("sellId", "s01"))
            .andExpect(status().isOk())
            .andDo(document("buy/getAll_filterBySellId",
                queryParameters(
                    parameterWithName("sellId").description("出品ID")
                )
                ));
    }

    @Test
    void getById() throws Exception {

        doReturn(createBuy(1)).when(this.buyService).getByIdAndUserId(any(), any());
        this.mockMvc.perform(get("/buy/buys/{id}", "b01"))
            .andExpect(status().isOk())
            .andDo(document("buy/getById",
                pathParameters(
                    parameterWithName("id").description("購入ID")
                )
            ));
    }



    private Buy createBuy(int index) {
        Buy buy = new Buy();
        buy.setId(String.format("b%02d", index));
        buy.setSellId("s01");
        buy.setBuyDateTime(LocalDateTime.of(2025, 2, 2, 1, 1, index));
        Sell sell = new Sell();
        sell.setId("s01");
        sell.setProductName(String.format("pname%02d", index));
        sell.setPrice(1000 * (index + 1));
        sell.setStatus(Sell.Status.NEED_SHIPPING);
        sell.setShippedDateTime(LocalDateTime.of(2025, 2, 4, 2, 2, 2));
        sell.setCompletedDateTime(LocalDateTime.of(2025, 2, 5, 2, 2, 2));
        List<ProductImage> pis = new ArrayList<>();
        ProductImage pi = new ProductImage();
        pi.setId("pi01");
        pis.add(pi);
        pi = new ProductImage();
        pi.setId("pi02");
        pis.add(pi);
        sell.setProductImages(pis);
        buy.setSell(sell);
        return buy;
    }

}

