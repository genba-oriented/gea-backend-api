package com.example.fleamarket.api.config;

import com.example.fleamarket.api.EnableSecurityTest;
import com.example.fleamarket.api.user.controller.UserController;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@EnableSecurityTest
public class JwtVerificationTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Test
    void validToken() throws  Exception {
        User user = new User();
        user.setIdpUserId("sub01");
        doReturn(user).when(this.userService).getByIdpUserId("sub01");

        String token = JwtUtils.createToken("sub01", "aud01");
        this.mockMvc.perform(get("/user/users/me")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idpUserId").value("sub01"))
            ;
    }

    @Test
    void notValidAudience() throws Exception {

        String token = JwtUtils.createToken("sub01", "aud99");
        this.mockMvc.perform(get("/user/users/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isUnauthorized())
        ;

    }
}
