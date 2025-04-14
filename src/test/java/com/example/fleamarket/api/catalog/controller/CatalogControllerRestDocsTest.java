package com.example.fleamarket.api.catalog.controller;

import com.example.fleamarket.api.catalog.dto.SellDto;
import com.example.fleamarket.api.catalog.service.CatalogService;
import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
@AutoConfigureRestDocs
class CatalogControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CatalogService catalogService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getSells() throws Exception {
        List<SellDto> sells = new ArrayList<>();
        SellDto sell = new SellDto();
        sell.setId("s01");
        sell.setProductName("pname01");
        sell.setDescription(("desc01"));
        sell.setPrice(1000);
        sell.setUserId("u01");
        sell.setProductImageIds(List.of("pi01", "pi02"));
        sell.setSold(false);
        sells.add(sell);
        sell = new SellDto();
        sell.setId("s02");
        sell.setProductName("pname02");
        sell.setDescription(("desc02"));
        sell.setPrice(2000);
        sell.setUserId("u02");
        sell.setProductImageIds(List.of("pi03", "pi04"));
        sell.setSold(true);
        sells.add(sell);
        sell = new SellDto();
        sell.setId("s03");
        sell.setProductName("pname03");
        sell.setDescription(("desc03"));
        sell.setPrice(3000);
        sell.setUserId("u03");
        sell.setProductImageIds(List.of("pi05", "pi06"));
        sell.setSold(false);
        sells.add(sell);
        Pageable pageable = PageRequest.of(0, 3);
        Page<SellDto> page = new PageImpl<>(sells, pageable, 10);
        doReturn(page).when(this.catalogService).getSells(any(), any());
        this.mockMvc.perform(get("/catalog/sells")
                .param("keyword", "name")
                .param("page", "0")
                .param("size", "3")
            )
            .andExpect(status().isOk())
            .andDo(document("catalog/getSells",
                queryParameters(
                    parameterWithName("keyword").description("商品名(部分一致)。指定しなかった場合は全検索"),
                    parameterWithName("page").description("ページ番号(0始まり)"),
                    parameterWithName("size").description("取得件数")
                )
            ));
    }

    @Test
    void getProductImage() throws Exception {
        ProductImageData productImageData = new ProductImageData();
        productImageData.setData("{this is binary data}".getBytes());
        doReturn(productImageData).when(this.catalogService).getProductImageDataByProductImageId(any());

        this.mockMvc.perform(get("/catalog/sells/{sellId}/product-images/{productImageId}", "s01", "pi01"))
            .andExpect(status().isOk())
            .andDo(document("catalog/getProductImage",
                pathParameters(
                    parameterWithName("sellId").description("出品ID"),
                    parameterWithName("productImageId").description("商品画像ID")
                )));
    }


    @Test
    void getSell() throws Exception {

        SellDto sell = new SellDto();
        sell.setId("s01");
        sell.setProductName("pname01");
        sell.setDescription(("desc01"));
        sell.setPrice(1000);
        sell.setUserId("u01");
        sell.setSold(false);
        sell.setProductImageIds(List.of("pi01", "pi0"));

        doReturn(sell).when(this.catalogService).getById(any());

        this.mockMvc.perform(get("/catalog/sells/{id}", "s01"))
            .andExpect(status().isOk())
            .andDo(document("catalog/getSell",
                pathParameters(
                    parameterWithName("id").description("出品ID")
                )
            ));
    }

}

