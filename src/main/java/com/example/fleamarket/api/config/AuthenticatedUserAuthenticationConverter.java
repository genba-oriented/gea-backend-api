package com.example.fleamarket.api.config;

import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// @ComponentでBean定義すると、Controllerのスライステスト(@WebMvcTest)時に、
// Converterを実装していることで取り込まれてしまってUserServiceのBean定義も必要になってしまうため、
// @BeanメソッドでBean定義する
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedUserAuthenticationConverter implements Converter<Jwt, AuthenticatedUserAuthentication> {

    private final UserService userService;
    private final OidcUserInfoService oidcUserInfoService;

	@Override
	public AuthenticatedUserAuthentication convert(Jwt jwt) {

        String idpUserId = jwt.getClaim("sub");

		User user = this.userService.getByIdpUserId(idpUserId);
        if (user == null) {
            // 新規ユーザとみなしレコードを登録する
            String email = jwt.getClaim("email");
            if (email == null) {
                email = (String)this.oidcUserInfoService.getUserInfo(jwt).get("email");
            }
            try {
                user = this.userService.registerNotActivated(idpUserId, email);
            } catch (DataIntegrityViolationException ex) {
                log.warn("登録に失敗。同時に処理が走ったと思われる " + ex.getMessage());
                user = this.userService.getByIdpUserId(idpUserId);
            }
        }
        return new AuthenticatedUserAuthentication(user);
	}


}
