package com.example.fleamarket.api.config;

import com.example.fleamarket.api.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityDependenciesConfig {

	// @ComponentでBean定義すると、Controllerのスライステスト(@WebMvcTest)時に、
	// Converterを実装していることで取り込まれてしまってUserRepositoryのBean定義も必要になってしまうため、
	// @BeanメソッドでBean定義する
	@Bean
	public AuthenticatedUserAuthenticationConverter authenticatedUserAuthenticationConverter(
        UserService userService) {
		return new AuthenticatedUserAuthenticationConverter(userService);
	}

}
