package com.example.fleamarket.api.sell.controller;

import com.example.fleamarket.api.sell.entity.ProductImage;
import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.input.SellInput;
import com.example.fleamarket.api.sell.service.SellService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellController.class)
@AutoConfigureRestDocs
class SellControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SellService sellService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void register() throws Exception {

        Sell sell = new Sell();
        sell.setId("s99");
        doReturn(sell).when(this.sellService).register(any(), any());

        SellInput sellInput = new SellInput();
        sellInput.setProductName("pname99");
        sellInput.setDescription("desc99");
        sellInput.setPrice(999);

        this.mockMvc.perform(
                post("/sell/sells")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(sellInput))
            )
            .andExpect(status().isCreated())
            .andDo(document("sell/register",
                requestFields(
                    fieldWithPath("productName").description("商品名"),
                    fieldWithPath("description").description("説明"),
                    fieldWithPath("price").description("値段")
                ),
                responseHeaders(
                    headerWithName("Location").description("作成された出品データのURL"))
            ));
    }

    @Test
    void registerProductImage() throws Exception {
        ProductImage productImage = new ProductImage();
        productImage.setId("pi99");
        doReturn(productImage).when(this.sellService).registerProductImage(any(), any(), any());

        MockMultipartFile file = new MockMultipartFile("file", "foo.png", "image/png", "{this is binary data}".getBytes());
        this.mockMvc.perform(
                multipart("/sell/sells/{sellId}/product-images", "s01")
                    .file(file)
            )
            .andExpect(status().isCreated())
            .andDo(document("sell/registerProductImage",
                requestParts(
                    partWithName("file").description("画像データ")
                ),
                pathParameters(
                    parameterWithName("sellId").description("出品ID")
                ),
                responseHeaders(
                    headerWithName("Location").description("登録された商品画像のURL")
                )
            ));
    }

    @Test
    void updateProductImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "foo.png", "image/png", "{this is binary data}".getBytes());
        this.mockMvc.perform(
                multipart("/sell/sells/{sellId}/product-images/{productImageId}", "s01", "pi01")
                    .file(file)
            )
            .andExpect(status().isNoContent())
            .andDo(document("sell/updateProductImage",
                requestParts(
                    partWithName("file").description("画像データ")
                ),
                pathParameters(
                    parameterWithName("sellId").description("出品ID"),
                    parameterWithName("productImageId").description("商品画像ID")
                )
            ));

    }

    @Test
    void getAll() throws Exception {
        List<Sell> sells = new ArrayList<>();
        sells.add(createSell(1));
        sells.add(createSell(2));
        sells.add(createSell(3));

        doReturn(sells).when(this.sellService).getByUserId(any());

        this.mockMvc.perform(get("/sell/sells"))
            .andExpect(status().isOk())
            .andDo(document("sell/getAll"));
    }

    @Test
    void getSell() throws Exception {
        Sell sell = createSell(1);

        doReturn(sell).when(this.sellService).getByIdAndUserId(any(), any());
        this.mockMvc.perform(get("/sell/sells/{sellId}", "s01"))
            .andExpect(status().isOk())
            .andDo(document("sell/getSell"));

    }

    Sell createSell(int idx) {
        Sell sell = new Sell();
        sell.setId(String.format("s%02d", idx));
        sell.setProductName(String.format("pname%02d", idx));
        sell.setDescription(String.format("desc%02d", idx));
        sell.setPrice(1000);
        sell.setUserId("u01");
        sell.setStatus(Sell.Status.NOW_SELLING);
        sell.setEditDateTime(LocalDateTime.of(2025, 2, 1, 10, 10, 10));
        sell.setSellDateTime(LocalDateTime.of(2025, 2, 3, 10, 10, 10));
        sell.setShippedDateTime(LocalDateTime.of(2025, 2, 4, 10, 10, 10));
        sell.setCompletedDateTime(LocalDateTime.of(2025, 2, 5, 10, 10, 10));
        List<ProductImage> productImages = new ArrayList<>();
        ProductImage productImage = new ProductImage();
        productImage.setId(String.format("pi%02d01", idx));
        productImages.add(productImage);
        productImage = new ProductImage();
        productImage.setId(String.format("pi%02d02", idx));
        productImages.add(productImage);
        sell.setProductImages(productImages);
        return sell;
    }

    @Test
    void getProductImage() throws Exception {
        ProductImageData productImageData = new ProductImageData();
        productImageData.setData("{this is binary data}".getBytes());
        doReturn(productImageData).when(this.sellService).getProductImageDataByProductImageIdAndUserId(any(), any());

        this.mockMvc.perform(get("/sell/sells/{sellId}/product-images/{productImageId}", "s01", "pi01"))
            .andExpect(status().isOk())
            .andDo(document("sell/getProductImage",
                pathParameters(
                    parameterWithName("sellId").description("出品ID"),
                    parameterWithName("productImageId").description("出品画像ID")
                )));
    }

    @Test
    void updateSell() throws Exception {
        SellInput sellInput = new SellInput();
        sellInput.setProductName("pname01");
        sellInput.setDescription("desc01");
        sellInput.setPrice(3000);

        this.mockMvc.perform(put("/sell/sells/{sellId}", "s01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sellInput))
            ).andExpect(status().isNoContent())
            .andDo(document("sell/updateSell",
                requestFields(
                    fieldWithPath("productName").description("商品名"),
                    fieldWithPath("description").description("説明"),
                    fieldWithPath("price").description("値段")
                ),
                pathParameters(
                    parameterWithName("sellId").description("出品ID")
                )
            ));


    }

    @Test
    void deleteProductImage() throws Exception {
        this.mockMvc.perform(delete("/sell/sells/{sellId}/product-images/{id}", "s01", "pi01"))
            .andExpect(status().isNoContent())
            .andDo(document("sell/deleteProductImage",
                pathParameters(
                    parameterWithName("sellId").description("出品ID"),
                    parameterWithName("id").description("商品画像ID")
                )
            ));
    }

    @Test
    void reorderProductImages() throws Exception {
        this.mockMvc.perform((put("/sell/sells/{sellId}/product-images/reorder", "s01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(List.of("pi02", "pi01")))
            ))
            .andExpect(status().isNoContent())
            .andDo(document("sell/reorderProductImages",
                pathParameters(
                    parameterWithName("sellId").description("出品ID")
                ),
                requestFields(
                    fieldWithPath("[]").description("商品画像IDの配列(並び替える順番)")
                )
            ));
    }

}
