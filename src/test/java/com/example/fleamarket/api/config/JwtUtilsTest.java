package com.example.fleamarket.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.BasicJsonTester;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;


class JwtUtilsTest {

    private BasicJsonTester json = new BasicJsonTester(getClass());
    @Test
    void createToken() {
        String token = JwtUtils.createToken("sub01", "aud01");
        String[] parts = token.split("\\.");
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        assertThat(json.from(payloadJson)).hasJsonPathValue("$.sub", "sub01");
        assertThat(json.from(payloadJson)).hasJsonPathValue("$.aud", "aud01");

    }

}
