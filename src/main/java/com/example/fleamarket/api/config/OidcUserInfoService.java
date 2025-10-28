package com.example.fleamarket.api.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OidcUserInfoService {

    private RestClient restClient = RestClient.builder().build();


    public Map<String, Object> getUserInfo(Jwt jwt) {
        Map<String, Object> idpMetadata = getIdpMetadata(jwt);
        String userInfoUrl = (String)idpMetadata.get("userinfo_endpoint");
        return this.restClient.get().uri(userInfoUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue())
            .retrieve().body(Map.class);
    }


    Map<String, Object> getIdpMetadata(Jwt jwt) {
        String issuer = jwt.getIssuer().toString();
        String discoveryUrl = issuer + "/.well-known/openid-configuration";
        return this.restClient.get().uri(discoveryUrl)
            .retrieve()
            .body(Map.class);
    }

}
