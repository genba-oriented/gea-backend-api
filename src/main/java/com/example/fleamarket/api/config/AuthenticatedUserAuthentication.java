package com.example.fleamarket.api.config;

import com.example.fleamarket.api.user.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

// JwtAuthenticationProviderが、AbstractAuthenticationTokenにキャストしているため、
// 直接Authenticationを実装せず、AbstractAuthenticationTokenを継承している
public class AuthenticatedUserAuthentication extends AbstractAuthenticationToken {

	private final User user;

    public AuthenticatedUserAuthentication(User user) {
        super(List.of());
        this.setAuthenticated(true);
        this.user = user;
    }

	@Override
	public User getPrincipal() {
		return this.user;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

}
