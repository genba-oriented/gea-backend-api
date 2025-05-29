package com.example.fleamarket.api.config;

import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

// @ComponentでBean定義すると、Controllerのスライステスト(@WebMvcTest)時に、
// Converterを実装していることで取り込まれてしまってUserServiceのBean定義も必要になってしまうため、
// @BeanメソッドでBean定義する
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedUserAuthenticationConverter implements Converter<Jwt, AuthenticatedUserAuthentication> {

    private final UserService userService;

	@Override
	@Transactional
	public AuthenticatedUserAuthentication convert(Jwt jwt) {

		String idpUserId = jwt.getClaim("sub");

		User user = this.userService.getByIdpUserId(idpUserId);
        if (user == null) {
            // 新規ユーザとみなしレコードを登録する
            try {
                user = this.userService.registerNotActivated(idpUserId, jwt.getClaim("email"));
            } catch (DataIntegrityViolationException ex) {
                log.warn("登録に失敗。同時に処理が走ったと思われる " + ex.getMessage());
                user = this.userService.getByIdpUserId(idpUserId);
            }
        }
        return new AuthenticatedUserAuthentication(user);
	}

}
