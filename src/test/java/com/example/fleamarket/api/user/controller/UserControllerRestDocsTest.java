package com.example.fleamarket.api.user.controller;

import com.example.fleamarket.api.EnableSecurityTest;
import com.example.fleamarket.api.WithUser;
import com.example.fleamarket.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@EnableSecurityTest
@AutoConfigureRestDocs
class UserControllerRestDocsTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Test
    @WithUser(id="u01", idpUserId = "idp01", name="uname01", shippingAddress = "address01", email = "u01@example.com", balance = 30000, activated = true)
    void me() throws Exception {
        this.mockMvc.perform(get("/user/users/me"))
            .andExpect(status().isOk())
            .andDo(document("user/me"));
    }



}
