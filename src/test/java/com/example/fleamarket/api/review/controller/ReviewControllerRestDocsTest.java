package com.example.fleamarket.api.review.controller;

import com.example.fleamarket.api.review.dto.RatedUserDto;
import com.example.fleamarket.api.review.entity.Review;
import com.example.fleamarket.api.review.exception.AlreadyReviewedException;
import com.example.fleamarket.api.review.input.ReviewInput;
import com.example.fleamarket.api.review.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@AutoConfigureRestDocs
class ReviewControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReviewService reviewService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void review() throws Exception {

        Review review = new Review();
        review.setId("r01");
        doReturn(review).when(this.reviewService).reviewAsBuyer(any(), any());

        ReviewInput reviewInput = new ReviewInput();
        reviewInput.setSellId("s01");
        reviewInput.setAsBuyer(true);
        reviewInput.setResult(ReviewInput.Result.GOOD);
        reviewInput.setComment("comment01");

        this.mockMvc.perform(post("/review/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(reviewInput))
        ).andExpect(status().isCreated())
            .andDo(document("review/review",
                requestFields(
                    fieldWithPath("sellId").description("出品ID"),
                    fieldWithPath("asBuyer").description("購入者:true、出品者:false"),
                    fieldWithPath("result").description("「GOOD」もしくは「BAD」"),
                    fieldWithPath("comment").description("コメント")
                ),
                responseHeaders(
                    headerWithName("Location").description("作成された評価データのURL"))
            ));

    }

    @Test
    void review_alreadyReviewed() throws Exception {
        doThrow(new AlreadyReviewedException("")).when(this.reviewService).reviewAsBuyer(any(), any());
        ReviewInput reviewInput = new ReviewInput();
        reviewInput.setSellId("s01");
        reviewInput.setAsBuyer(true);
        reviewInput.setResult(ReviewInput.Result.GOOD);
        reviewInput.setComment("comment01");
        this.mockMvc.perform(post("/review/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(reviewInput)))
            .andExpect(status().isBadRequest())
            .andDo(document("review/review_alreadyReviewed",
                responseFields(
                    fieldWithPath("cause").description("エラー原因 AlreadyReviewed:すでに評価されている")
                )
            ));

    }

    @Test
    void getRatedUser() throws Exception {
        RatedUserDto dto = new RatedUserDto();
        dto.setUserId("u01");
        dto.setUserName("uname01");
        dto.setReviewCount(10);
        dto.setAverageScore(4.3d);
        doReturn(dto).when(this.reviewService).getRatedUser(any());

        this.mockMvc.perform(get("/review/rated-users/{userId}", "u01")
        ).andExpect(status().isOk())
            .andDo(document("review/getRatedUser",
                pathParameters(
                    parameterWithName("userId").description("ユーザID")
                )
            ));

    }

}
