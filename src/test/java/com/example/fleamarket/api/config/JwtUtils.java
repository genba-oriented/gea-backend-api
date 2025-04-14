package com.example.fleamarket.api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.OffsetDateTime;
import java.util.Base64;

public class JwtUtils {
    static String createToken(String sub, String audience) {
        RSAPrivateKey privateKey = getPrivateKey();
        Algorithm alg = Algorithm.RSA256(privateKey);
        return JWT.create()
            .withIssuer("test-issuer")
            .withSubject(sub)
            .withAudience(audience)
            .withExpiresAt(OffsetDateTime.now().plusMinutes(60).toInstant())
            .withIssuedAt(OffsetDateTime.now().toInstant())
            .sign(alg);
    }

    // https://zenn.dev/htlsne/articles/rsa-key-format
    // openssl genrsa -out test.key 1024
    // openssl rsa -in test.key -pubout -out test.key.pub
    static RSAPrivateKey getPrivateKey() {
        Resource file = new ClassPathResource("test.key");
        try {
            String privateKeyStr = file.getContentAsString(StandardCharsets.UTF_8);
            privateKeyStr = privateKeyStr
                .replaceAll("-----BEGIN.*-----", "")
                .replaceAll("-----END.*-----", "");
            byte[] encoded = Base64.getMimeDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
