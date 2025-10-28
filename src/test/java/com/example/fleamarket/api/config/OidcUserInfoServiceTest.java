package com.example.fleamarket.api.config;

import com.example.fleamarket.api.AbstractKeycloakContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OidcUserInfoServiceTest extends AbstractKeycloakContainerTest {

    @Test
    void test_getUserInfo() {
        String issuerUrl = keycloak.getAuthServerUrl() + "/realms/master";
        String accessToken = getAccessToken();
        Jwt jwt = Jwt.withTokenValue(accessToken)
            .header("alg", "none")
            .issuer(issuerUrl).build();
        OidcUserInfoService userInfoService = new OidcUserInfoService();
        Map<String, Object> userInfo = userInfoService.getUserInfo(jwt);
        assertThat(userInfo.get("email")).isEqualTo("taro@example.com");
    }


    @Test
    void test_getIdpMetadata() {
        Map<String, Object> idpMetadata = getIdpMetadata();
        assertThat(idpMetadata.get("authorization_endpoint")).isNotNull();
    }

    Map<String ,Object> getIdpMetadata() {
        String issuerUrl = keycloak.getAuthServerUrl() + "/realms/master";
        Jwt jwt = Jwt.withTokenValue("foo")
            .header("alg", "none")
            .issuer(issuerUrl).build();
        OidcUserInfoService userInfoService = new OidcUserInfoService();
        return userInfoService.getIdpMetadata(jwt);
    }


    String getAccessToken() {
        Map<String, Object> idpMetadata = getIdpMetadata();
        String tokenUrl = (String)idpMetadata.get("token_endpoint");
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "taro");
        map.add("password", "taro");
        map.add("scope", "openid email");
        RestClient restClient = RestClient.builder().build();
        Map tokens = restClient.post().uri(tokenUrl)
            .header("Authorization", "Basic " + "Zm9yLXRlc3Q6NHdIVW56WXNwbDlzRUhPTE04RnBKbWVxekZpQUM4UlA=")
            .body(map)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).retrieve().body(Map.class);
        return (String)tokens.get("access_token");
    }
}
