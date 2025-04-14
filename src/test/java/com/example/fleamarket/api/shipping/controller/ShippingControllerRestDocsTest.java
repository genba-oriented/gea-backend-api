package com.example.fleamarket.api.shipping.controller;

import com.example.fleamarket.api.shipping.exception.AlreadyShippedException;
import com.example.fleamarket.api.shipping.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShippingController.class)
@AutoConfigureRestDocs
class ShippingControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShippingService shippingService;

    @Test
    void shipped() throws Exception {
        this.mockMvc.perform(put("/shipping/sells/{sellId}/shipped", "s01"))
            .andExpect(status().isNoContent())
            .andDo(document("shipping/shipped",
                pathParameters(
                    parameterWithName("sellId").description("出品ID")
                )
            ));
    }

    @Test
    void shipped_alreadyShipped() throws Exception {
        doThrow(new AlreadyShippedException("foo")).when(this.shippingService).shipped(any(), any());

        this.mockMvc.perform(put("/shipping/sells/{sellId}/shipped", "s01"))
            .andExpect(status().isBadRequest())
            .andDo(document("shipping/shipped_alreadyShipped",
                responseFields(
                    fieldWithPath("cause").description("エラー原因 AlreadyShipped:すでに発送されている")
                )
            ));

    }


}
